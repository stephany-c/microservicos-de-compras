import { useState } from 'react';
import { userApi } from '../services/api';
import { useNavigate, Link } from 'react-router-dom';
import { ShoppingCart } from 'lucide-react';

export const Login = ({ onLogin }: { onLogin: () => void }) => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleLogin = async (e: React.FormEvent) => {
    e.preventDefault();
    try {
      const response = await userApi.post('/login', { email, password });
      const { token } = response.data;
      localStorage.setItem('token', token);
      onLogin();
      navigate('/');
    } catch (err) {
      setError('Credenciais inválidas ou erro no servidor.');
    }
  };

  return (
    <div style={{ display: 'flex', height: '100vh', alignItems: 'center', justifyContent: 'center', backgroundColor: 'var(--bg-primary)' }}>
      <div className="card" style={{ width: '400px' }}>
        <div style={{ textAlign: 'center', marginBottom: '2rem' }}>
          <ShoppingCart size={48} color="var(--accent-primary)" style={{ margin: '0 auto' }} />
          <h2 style={{ marginTop: '1rem' }}>ShopFlow Login</h2>
        </div>
        
        {error && <div style={{ color: 'var(--danger)', marginBottom: '1rem', textAlign: 'center' }}>{error}</div>}
        
        <form onSubmit={handleLogin}>
          <div className="form-group">
            <label className="form-label">Email</label>
            <input 
              type="email" 
              className="form-control" 
              value={email} 
              onChange={e => setEmail(e.target.value)} 
              required 
            />
          </div>
          <div className="form-group">
            <label className="form-label">Senha</label>
            <input 
              type="password" 
              className="form-control" 
              value={password} 
              onChange={e => setPassword(e.target.value)} 
              required 
            />
          </div>
          <button type="submit" className="btn btn-primary" style={{ width: '100%' }}>Entrar</button>
        </form>
        
        <div style={{ marginTop: '1.5rem', textAlign: 'center' }}>
          <p style={{ color: 'var(--text-secondary)' }}>
            Ainda não tem conta? <Link to="/register" style={{ color: 'var(--accent-primary)', textDecoration: 'none' }}>Cadastre-se</Link>
          </p>
        </div>
      </div>
    </div>
  );
};
