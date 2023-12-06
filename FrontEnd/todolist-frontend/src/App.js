import './App.css';
import Layout from './components/Layout';
import LinkPage from './components/LinkPage';
import Login from './components/Login';
import Register from './components/Register';
import Show from './components/Show';
import { Routes, Route } from 'react-router-dom';

function App() {
  return (
    <Routes>
      <Route path='/' element={<Layout />} >
        {/* public routes */}
        <Route path='/login' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/linkpage' element={<LinkPage />} />

        {/* we want to protected this routes */}
        <Route path='/' element={<Show />} />
      </Route>
    </Routes>
  );
}

export default App;