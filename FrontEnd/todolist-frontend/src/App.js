import './App.css';
import Layout from './components/Layout';
import Login from './components/Login';
import Register from './components/Register';
import LinkPage from './components/LinkPage';
import Unauthorized from './components/Unauthorized';
import Show from './components/Show';
import Admin from './components/Admin';
import { Routes, Route } from 'react-router-dom';
import RequireAuth from './components/RequireAuth';

const ROLES = {
  'User': 'ROLE_CLIENT',
  'Admin': 'ROLE_ADMIN'
}

function App() {
  return (
    <Routes>
      <Route path='/' element={<Layout />} >
        {/* public routes */}
        <Route path='/login' element={<Login />} />
        <Route path='/register' element={<Register />} />
        <Route path='/linkpage' element={<LinkPage />} />
        <Route path="unauthorized" element={<Unauthorized />} />

        {/* we want to protected this routes */}
        <Route element={<RequireAuth allowedRoles={[ROLES.User, ROLES.Admin]} />}>
          <Route path='/' element={<Show />} />
        </Route>
        <Route element={<RequireAuth allowedRoles={[ROLES.Admin]} />}>
          <Route path='/admin' element={<Admin />} />
        </Route>
      </Route>
    </Routes >
  );
}

export default App;