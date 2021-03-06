package net.unesc.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.JOptionPane;
import net.unesc.banco.*;
import net.unesc.entidades.*;
import net.unesc.exceptions.BancoException;
import net.unesc.exceptions.CampoObrigatorioException;
import net.unesc.exceptions.DataException;
import net.unesc.exceptions.FormatoDataException;
import net.unesc.log.LogSistema;
import net.unesc.log.TipoLog;
import net.unesc.utilidades.DiaHora;

public class RegraEventoDao extends DaoPadrao {
    public void inserir(Regra regra) throws BancoException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        try {
            conn = Conexao.getConnection();
            Integer proxSequencia = Funcoes.obterSequencia("regra_evento");
            
            String sql = "insert into regra_evento (nr_sequencia, nm_usuario, dt_inclusao, "
                    + "ds_regra, dt_inicio_vigencia, dt_fim_vigencia, ie_situacao, ie_tipo_horario, "
                    + "qt_hh, qt_mm, qt_ss, qt_ml, dia_dom, dia_seg, dia_ter, dia_qua, dia_qui, dia_sex, dia_sab)"
                    + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, proxSequencia);
            ps.setString(2, regra.getUsuario().getLogin());
            ps.setString(3, DiaHora.obterDiaHora("yyyy-MM-dd HH:mm:ss"));
            ps.setString(4, regra.getNome());
            ps.setString(5, DiaHora.formatarData(regra.getInicioVigencia(),"yyyy-MM-dd HH:mm:ss"));
            ps.setString(6, DiaHora.formatarData(regra.getFimVigencia(),"yyyy-MM-dd HH:mm:ss"));
            ps.setString(7, regra.getSituacao());
            ps.setString(8, regra.getTipoHorario());
            ps.setInt(9, regra.getHora());
            ps.setInt(10, regra.getMinuto());
            ps.setInt(11, regra.getSegundo());
            ps.setInt(12, regra.getMilesimos());
            ps.setBoolean(13, regra.getDiaSemana(0));
            ps.setBoolean(14, regra.getDiaSemana(1));
            ps.setBoolean(15, regra.getDiaSemana(2));
            ps.setBoolean(16, regra.getDiaSemana(3));
            ps.setBoolean(17, regra.getDiaSemana(4));
            ps.setBoolean(18, regra.getDiaSemana(5));
            ps.setBoolean(19, regra.getDiaSemana(6));
            
            ps.execute();
            conn.commit();
            
            regra.setCodigo(proxSequencia);
            LogSistema.inserir(TipoLog.INCLUSAO, "Gravou uma nova Regra do Evento");
        } catch(SQLException e) {
            erro(conn, "Erro ao cadastrar a regra", e);
        } finally {
            finaliza(conn, ps);
        }
    }
    
    public void atualizar(Regra regra) throws BancoException {
        Connection conn = null;
        PreparedStatement ps = null;
        
        if (regra == null || regra.getCodigo() == null)
            erro(conn, "Erro ao cadastrar a regra", null);
        
        try {
            conn = Conexao.getConnection();
            Integer proxSequencia = Funcoes.obterSequencia("regra_evento");
            
            String sql = "update regra_evento set ds_regra = ? , dt_inicio_vigencia = ?, dt_fim_vigencia = ?, "
                    + "ie_situacao = ?, ie_tipo_horario = ?, "
                    + "qt_hh = ?, qt_mm = ?, qt_ss = ?, qt_ml = ?, dia_dom = ?, dia_seg = ?, "
                    + "dia_ter = ?, dia_qua = ?, dia_qui = ?, dia_sex = ?, dia_sab = ? "
                    + "where nr_sequencia = ?";
            
            ps = conn.prepareStatement(sql);
            ps.setString(1, regra.getNome());
            ps.setString(2, DiaHora.formatarData(regra.getInicioVigencia(),"yyyy-MM-dd HH:mm:ss"));
            ps.setString(3, DiaHora.formatarData(regra.getFimVigencia(),"yyyy-MM-dd HH:mm:ss"));
            ps.setString(4, regra.getSituacao());
            ps.setString(5, regra.getTipoHorario());
            ps.setInt(6, regra.getHora());
            ps.setInt(7, regra.getMinuto());
            ps.setInt(8, regra.getSegundo());
            ps.setInt(9, regra.getMilesimos());
            ps.setBoolean(10, regra.getDiaSemana(0));
            ps.setBoolean(11, regra.getDiaSemana(1));
            ps.setBoolean(12, regra.getDiaSemana(2));
            ps.setBoolean(13, regra.getDiaSemana(3));
            ps.setBoolean(14, regra.getDiaSemana(4));
            ps.setBoolean(15, regra.getDiaSemana(5));
            ps.setBoolean(16, regra.getDiaSemana(6));
            ps.setInt(17, regra.getCodigo());
            ps.execute();
            conn.commit();
            
            regra.setCodigo(proxSequencia);
            LogSistema.inserir(TipoLog.INCLUSAO, "Atualizou a regra: "+regra.getCodigo());
        } catch(SQLException e) {
            erro(conn, "Erro ao cadastrar a regra", e);
        } finally {
            finaliza(conn, ps);
        }
    }
    
    public List<Regra> getAll() throws BancoException {
        List<Regra> lista = new ArrayList<Regra>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Conexao.getConnection();
            String sql = 
                " select nr_sequencia, " +
                " nm_usuario," +
                " dt_inclusao, " +
                " ds_regra," +
                " dt_inicio_vigencia," +
                " dt_fim_vigencia," +
                " ie_situacao," +
                " ie_tipo_horario," +
                " qt_hh," +
                " qt_mm," +
                " qt_ss," +
                " qt_ml," +
                " dia_dom," +
                " dia_seg," +
                " dia_ter," +
                " dia_qua," +
                " dia_qui," +
                " dia_sex," +
                " dia_sab" +
                " from regra_evento";
            ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
            while(rs.next()) {
                Regra p = new Regra();
                p.setCodigo(rs.getInt(1));
                p.setNome(rs.getString(4));
                p.setInicioVigencia(rs.getDate(5));
                p.setFimVigencia(rs.getDate(6));
                p.setSituacao(rs.getString(7));
                p.setTipoHorario(rs.getString(8));
                
                p.setHora(rs.getInt(9));
                p.setMinuto(rs.getInt(10));
                p.setSegundo(rs.getInt(11));
                p.setMilesimos(rs.getInt(12));
                
                if(rs.getInt(13) == 0){
                    p.setDiaSemana(0, true);
                }else{
                    p.setDiaSemana(0, false);
                }
                
                if(rs.getInt(14) == 0){
                    p.setDiaSemana(1, true);
                }else{
                    p.setDiaSemana(1, false);
                }
                
                if(rs.getInt(15) == 0){
                    p.setDiaSemana(2, true);
                }else{
                    p.setDiaSemana(2, false);
                }
                
                if(rs.getInt(16) == 0){
                    p.setDiaSemana(3, true);
                }else{
                    p.setDiaSemana(3, false);
                }
                
                if(rs.getInt(17) == 0){
                    p.setDiaSemana(4, true);
                }else{
                    p.setDiaSemana(4, false);
                }
                
                if(rs.getInt(18) == 0){
                    p.setDiaSemana(5, true);
                }else{
                    p.setDiaSemana(5, false);
                }
                
                if(rs.getInt(19) == 0){
                    p.setDiaSemana(6, true);
                }else{
                    p.setDiaSemana(6, false);
                }
                
                lista.add(p);
            }
        } catch(SQLException|CampoObrigatorioException|DataException e) {
            erro(conn, "Erro ao buscar as regras", e);
        } finally {
            finaliza(conn, ps);
        }
        return lista;
    }
    
    public ArrayList<Regra> getRegras(Integer codigo)throws BancoException, CampoObrigatorioException, DataException, FormatoDataException{
        ArrayList<Regra> lista = new ArrayList<Regra>();
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Conexao.getConnection();
            String sql = 
                " select * "+
                " from regra_evento "+
                " where nr_sequencia = ? ";       
            
            ps = conn.prepareStatement(sql);
            ps.setInt(1, codigo);
            
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Regra p = new Regra();
                p.setCodigo(rs.getInt(1));
                p.setNome(rs.getString(4));
                p.setInicioVigencia(rs.getDate(5));
                p.setFimVigencia(rs.getDate(6));
                p.setSituacao(rs.getString(7));
                p.setTipoHorario(rs.getString(8));
                p.setHora(rs.getString(9));
                p.setMinuto(rs.getString(10));
                p.setSegundo(rs.getString(11));
                p.setMilesimos(rs.getString(12));
                lista.add(p);
            }
        } catch(SQLException e) {
            erro(conn, "Erro ao buscar as regras", e);
        } finally {
            finaliza(conn, ps);
        }
        return lista;
    }
    
    public void excluirRegra(Regra regra) throws BancoException{
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = Conexao.getConnection();
            String sql = "delete from regra_evento where nr_sequencia = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, regra.getCodigo());
            ps.execute();
            conn.commit();
            LogSistema.inserir(TipoLog.EXCLUSAO, "Excluiu um Cadastro de Regra");
        } catch(SQLException e) {
            erro(conn, "Erro ao excluir Regra", e);
        } finally {
            finaliza(conn, ps);
        }
    }
    
}
