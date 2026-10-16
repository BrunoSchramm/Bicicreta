package MODEL;

public class Bicicleta {
    private Integer id;
    private Integer idCliente;
    private String marcaModelo;
    private String cor;
    private String numeroSerie;

    public Bicicleta() {}

    public Bicicleta(Integer id, Integer idCliente, String marcaModelo, String cor, String numeroSerie) {
        this.id = id;
        this.idCliente = idCliente;
        this.marcaModelo = marcaModelo;
        this.cor = cor;
        this.numeroSerie = numeroSerie;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }

    public String getMarcaModelo() { return marcaModelo; }
    public void setMarcaModelo(String marcaModelo) { this.marcaModelo = marcaModelo; }

    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }

    public String getNumeroSerie() { return numeroSerie; }
    public void setNumeroSerie(String numeroSerie) { this.numeroSerie = numeroSerie; }
}