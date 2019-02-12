package br.feevale.ameacasambientais;

/**
 * Created by adonis on 17/10/16.
 */
public class Ameaca {
    private Integer  id;
    private String   descricao;
    private String   endereco;
    private String   bairro;
    private Integer  potencial_impacto;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getPotencialImpacto() {
        return potencial_impacto;
    }

    public void setPotencialImpacto(Integer potencial_impacto) {
        this.potencial_impacto = potencial_impacto;
    }
}
