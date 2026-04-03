package rgonzales.app.labinventario.model;

public class Categoria {
    private int id;
    private String nombre;
    private String fechaCreacion;
    private int totalProductos;

    public Categoria(int id, String nombre, String fechaCreacion, int totalProductos) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCreacion = fechaCreacion;
        this.totalProductos = totalProductos;
    }
    public Categoria() {
        this.id = 0;
        this.nombre = "";
        this.fechaCreacion = "";
        this.totalProductos = 0;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
    public int getTotalProductos() {
        return totalProductos;
    }

    public void setTotalProductos(int totalProductos) {
        this.totalProductos = totalProductos;
    }
}
