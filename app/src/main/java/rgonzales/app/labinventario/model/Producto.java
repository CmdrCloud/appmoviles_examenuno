package rgonzales.app.labinventario.model;

public class Producto {
    private int id;
    private String nombre;
    private double precio;
    private int cantidad;
    private int idCategoria;
    private String fechaCreacion ;

    public Producto(int id, String nombre, double precio, int cantidad, int idCategoria, String fechaCreacion) {
        this.id = id;
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.idCategoria = idCategoria;
        this.fechaCreacion = fechaCreacion;
    }
    public Producto() {
        this.id = 0;
        this.nombre = "";
        this.precio = 0;
        this.cantidad = 0;
        this.idCategoria = 0;
        this.fechaCreacion = "";
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

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}
