import { useState } from 'react';
import { productApi } from '../services/api';
import { Plus, ArrowLeft } from 'lucide-react';
import { Link } from 'react-router-dom';

export const AdminProducts = () => {
  const [name, setName] = useState('');
  const [preco, setPreco] = useState('');
  const [quantidade, setQuantidade] = useState('');

  const handleCreateProduct = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      await productApi.post('/products', {
        name,
        preco: parseFloat(preco),
        quantidade: parseInt(quantidade, 10)
      });
      alert("Produto criado com sucesso!");
      setName('');
      setPreco('');
      setQuantidade('');
    } catch (e) {
      alert("Erro ao criar produto.");
    }
  };

  return (
    <div>
      <div style={{ display: 'flex', alignItems: 'center', gap: '1rem', marginBottom: '2rem' }}>
        <Link to="/" className="btn btn-secondary" style={{ padding: '0.5rem' }}><ArrowLeft size={20} /></Link>
        <h1 className="page-title" style={{ margin: 0 }}>Administração de Produtos</h1>
      </div>

      <div className="card" style={{ maxWidth: '600px', margin: '0 auto' }}>
        <h3>Cadastrar Novo Produto no Sistema</h3>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '1.5rem' }}>Esta página é restrita para administradores.</p>
        <form onSubmit={handleCreateProduct}>
          <div className="form-group">
            <label className="form-label">Nome do Produto</label>
            <input type="text" className="form-control" value={name} onChange={e => setName(e.target.value)} required />
          </div>
          <div className="grid grid-cols-2">
            <div className="form-group">
              <label className="form-label">Preço (R$)</label>
              <input type="number" step="0.01" className="form-control" value={preco} onChange={e => setPreco(e.target.value)} required />
            </div>
            <div className="form-group">
              <label className="form-label">Estoque (Qtd Inicial)</label>
              <input type="number" className="form-control" value={quantidade} onChange={e => setQuantidade(e.target.value)} required />
            </div>
          </div>
          <button type="submit" className="btn btn-primary" style={{ width: '100%' }}><Plus size={18}/> Salvar Produto</button>
        </form>
      </div>
    </div>
  );
};
