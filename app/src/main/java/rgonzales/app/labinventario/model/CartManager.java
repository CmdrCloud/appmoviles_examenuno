package rgonzales.app.labinventario.model;

import java.util.ArrayList;
import java.util.List;

public class CartManager {
    private static CartManager instance;
    private List<CartItem> cartItems;

    private CartManager() {
        cartItems = new ArrayList<>();
    }

    public static synchronized CartManager getInstance() {
        if (instance == null) {
            instance = new CartManager();
        }
        return instance;
    }

    public void addProducto(Producto producto) {
        for (CartItem item : cartItems) {
            if (item.getProducto().getId() == producto.getId()) {
                item.setCantidad(item.getCantidad() + 1);
                return;
            }
        }
        cartItems.add(new CartItem(producto, 1));
    }

    public List<CartItem> getCartItems() {
        return cartItems;
    }

    public double getSubtotal() {
        double subtotal = 0;
        for (CartItem item : cartItems) {
            subtotal += item.getProducto().getPrecio() * item.getCantidad();
        }
        return subtotal;
    }

    public void clearCart() {
        cartItems.clear();
    }
}
