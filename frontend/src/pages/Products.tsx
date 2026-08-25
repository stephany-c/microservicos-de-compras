import { useState, useEffect } from 'react';
import { productApi, cartApi, userApi } from '../services/api';
import { jwtDecode } from 'jwt-decode';
import { ShoppingCart } from 'lucide-react';

interface Product {
  id: string;
  name: string;
  preco: number;
  quantidade: number;
}

export const Products = () => {
  const [products, setProducts] = useState<Product[]>([]);
  const [loading, setLoading] = useState(true);
  const [userId, setUserId] = useState<string | null>(null);

  useEffect(() => {
    fetchProducts();
    fetchUserId();
  }, []);

  const fetchUserId = async () => {
    try {
      const token = localStorage.getItem('token');
      if (token) {
        const decoded: any = jwtDecode(token);
        const email = decoded.sub;
        const response = await userApi.get(`/users/email?email=${email}`);
        setUserId(response.data.id);
      }
    } catch (e) {
      console.error("Erro ao buscar usuário logado", e);
    }
  };

  const fetchProducts = async () => {
    try {
      const res = await productApi.get('/products');
      setProducts(res.data);
    } catch (e) {
      console.error("Erro ao buscar produtos", e);
    } finally {
      setLoading(false);
    }
  };

  const addToCart = async (product: Product) => {
    if (!userId) {
      alert("Usuário não identificado.");
      return;
    }
    try {
      await cartApi.post(`/carts/${userId}/items`, {
        productId: product.id,
        quantity: 1
      });
      alert(`${product.name} adicionado ao carrinho!`);
    } catch (e) {
      alert("Erro ao adicionar ao carrinho.");
    }
  };

  return (
    <div>
      <div style={{ textAlign: 'center', marginBottom: '3rem' }}>
        <h1 style={{ fontSize: '2.5rem', marginBottom: '0.5rem', fontWeight: '800' }}>Bem-vindo à ShopFlow</h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '1.2rem' }}>Encontre os melhores produtos com os melhores preços!</p>
      </div>

      {loading ? <p style={{ textAlign: 'center' }}>Carregando a vitrine...</p> : (
        <div className="grid grid-cols-4">
          {products.map(p => (
            <div key={p.id} className="card" style={{ display: 'flex', flexDirection: 'column', transition: 'transform 0.2s', cursor: 'pointer' }} onMouseEnter={(e) => e.currentTarget.style.transform = 'translateY(-5px)'} onMouseLeave={(e) => e.currentTarget.style.transform = 'translateY(0)'}>
              <div style={{ height: '150px', backgroundColor: 'var(--bg-primary)', borderRadius: '8px', marginBottom: '1rem', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
                <span style={{ color: 'var(--text-secondary)' }}>Sem Imagem</span>
              </div>
              <h4 style={{ fontSize: '1.2rem', marginBottom: '0.5rem' }}>{p.name}</h4>
              <p style={{ color: 'var(--text-secondary)', marginBottom: '1rem', fontSize: '0.9rem' }}>Disponível: {p.quantidade} un.</p>
              <div style={{ marginTop: 'auto', display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
                <span style={{ fontSize: '1.3rem', fontWeight: 'bold', color: 'var(--accent-primary)' }}>R$ {p.preco.toFixed(2)}</span>
                <button onClick={() => addToCart(p)} className="btn btn-primary" style={{ padding: '0.6rem', borderRadius: '50%' }} title="Adicionar ao Carrinho">
                  <ShoppingCart size={18} />
                </button>
              </div>
            </div>
          ))}
          {products.length === 0 && (
            <div style={{ gridColumn: 'span 4', textAlign: 'center', padding: '3rem' }} className="card">
              <p>Nenhum produto disponível no momento.</p>
            </div>
          )}
        </div>
      )}
    </div>
  );
};
