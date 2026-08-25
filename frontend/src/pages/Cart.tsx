import { useState, useEffect } from 'react';
import { cartApi, productApi, userApi, orderApi } from '../services/api';
import { jwtDecode } from 'jwt-decode';
import { Trash2, CreditCard } from 'lucide-react';

interface CartItem {
  productId: string;
  quantity: number;
  name?: string;
  preco?: number;
}

interface Cart {
  id: string;
  userId: string;
  status: string;
  items: CartItem[];
}

export const Cart = () => {
  const [cart, setCart] = useState<Cart | null>(null);
  const [loading, setLoading] = useState(true);
  const [userId, setUserId] = useState<string | null>(null);

  useEffect(() => {
    fetchUserIdAndCart();
  }, []);

  const fetchUserIdAndCart = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const decoded: any = jwtDecode(token);
        const email = decoded.sub;
        const userRes = await userApi.get(`/users/email?email=${email}`);
        const uid = userRes.data.id;
        setUserId(uid);
        
        await fetchCart(uid);
      }
    } catch (e) {
      console.error(e);
      setLoading(false);
    }
  };

  const fetchCart = async (uid: string) => {
    try {
      const res = await cartApi.get(`/carts/${uid}`);
      let fetchedCart: Cart = res.data;

      const enrichedItems = await Promise.all(fetchedCart.items.map(async (item) => {
        try {
          const prodRes = await productApi.get(`/products/${item.productId}`);
          return { ...item, name: prodRes.data.name, preco: prodRes.data.preco };
        } catch (e) {
          return { ...item, name: 'Produto Desconhecido', preco: 0 };
        }
      }));

      fetchedCart.items = enrichedItems;
      setCart(fetchedCart);
    } catch (e) {
      console.error("Erro ao buscar carrinho", e);
    } finally {
      setLoading(false);
    }
  };

  const removeItem = async (productId: string) => {
    if (!userId) return;
    try {
      await cartApi.delete(`/carts/${userId}/items/${productId}`);
      fetchCart(userId);
    } catch (e) {
      alert("Erro ao remover item.");
    }
  };

  const handleCheckout = async () => {
    if (!userId || !cart || cart.items.length === 0) return;
    
    try {
      await Promise.all(cart.items.map(i => 
        orderApi.post('/orders', {
          userId: userId,
          productId: i.productId
        })
      ));
      
      await Promise.all(cart.items.map(i => 
        cartApi.delete(`/carts/${userId}/items/${i.productId}`)
      ));
      
      alert("Pedidos realizados com sucesso!");
      fetchCart(userId);
    } catch (e) {
      alert("Erro ao finalizar os pedidos.");
      console.error(e);
    }
  };

  const total = cart?.items.reduce((acc, item) => acc + (item.preco || 0) * item.quantity, 0) || 0;

  return (
    <div>
      <h1 className="page-title">Meu Carrinho</h1>

      {loading ? <p>Carregando...</p> : (
        cart && cart.items.length > 0 ? (
          <div className="grid grid-cols-3">
            <div className="card" style={{ gridColumn: 'span 2' }}>
              <div className="table-container">
                <table>
                  <thead>
                    <tr>
                      <th>Produto</th>
                      <th>Preço</th>
                      <th>Qtd</th>
                      <th>Subtotal</th>
                      <th></th>
                    </tr>
                  </thead>
                  <tbody>
                    {cart.items.map(item => (
                      <tr key={item.productId}>
                        <td>{item.name}</td>
                        <td>R$ {item.preco?.toFixed(2)}</td>
                        <td>{item.quantity}</td>
                        <td>R$ {((item.preco || 0) * item.quantity).toFixed(2)}</td>
                        <td>
                          <button onClick={() => removeItem(item.productId)} className="btn btn-danger" style={{ padding: '0.4rem' }}>
                            <Trash2 size={16} />
                          </button>
                        </td>
                      </tr>
                    ))}
                  </tbody>
                </table>
              </div>
            </div>
            
            <div className="card" style={{ height: 'fit-content' }}>
              <h3>Resumo do Pedido</h3>
              <div style={{ display: 'flex', justifyContent: 'space-between', marginTop: '1.5rem', marginBottom: '1.5rem' }}>
                <span style={{ fontSize: '1.2rem', fontWeight: 'bold' }}>Total:</span>
                <span style={{ fontSize: '1.2rem', fontWeight: 'bold', color: 'var(--accent-primary)' }}>R$ {total.toFixed(2)}</span>
              </div>
              <button onClick={handleCheckout} className="btn btn-primary" style={{ width: '100%' }}>
                <CreditCard size={18} /> Finalizar Compra
              </button>
            </div>
          </div>
        ) : (
          <div className="card">
            <p>Seu carrinho está vazio.</p>
          </div>
        )
      )}
    </div>
  );
};
