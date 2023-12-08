import {useRef, useState, useEffect} from "react";
import useAuth from "../hooks/useAuth";
import {Link, useNavigate, useLocation} from "react-router-dom";
import axios from "../api/axios";

const LOGIN_URL = '/login';

const Login = () => {
    const {setAuth} = useAuth();
    const navigate = useNavigate();
    const location = useLocation();
    const from = location.state?.from?.pathname || '/';

    const userRef = useRef();
    const errRef = useRef();

    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [errMsg, setErrMsg] = useState('');

    useEffect(() => {
        userRef.current.focus();
    }, [])

    useEffect(() => {
        setErrMsg('');
    }, [userName, password])

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                LOGIN_URL,
                JSON.stringify({userName, password}),
                {
                    headers: {'Content-Type': 'application/json'},
                    withCredentials: true
                }
            ).then((res) => {
                return res.data.data;
            });
            const token = response.token;
            const rol = JSON.stringify(response.role);
            const arrRol = Array([rol]);
            const type = typeof (arrRol);
            console.log('type: ' + type);
            console.log('rol : ' + arrRol);
            console.log('token : ' + token);
            setAuth({userName, password, arrRol, token});
            setUserName('');
            setPassword('');
            navigate(from, {replace: true});
        } catch (err) {
            if (!err?.response) {
                setErrMsg('no server response');
            } else if (err.response?.status === 400) {
                setErrMsg('missing username or password');
            } else if (err.response?.status === 401) {
                setErrMsg('unauthorized');
            } else {
                setErrMsg('login failed')
            }
            errRef.current.focus();
        }
    }

    return (
        <section>
            <p ref={errRef} className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">
                {errMsg}
            </p>
            <h1>Sign In</h1>
            <form onSubmit={handleSubmit}>
                <label htmlFor="username">Username: </label>
                <input
                    id="username"
                    type="text"
                    ref={userRef}
                    onChange={(e) => setUserName(e.target.value)}
                    value={userName}
                    required
                />

                <label htmlFor="password">Password: </label>
                <input
                    id="password"
                    type="password"
                    onChange={(e) => setPassword(e.target.value)}
                    value={password}
                    required
                />
                <button>Sign In</button>
            </form>
            <p>
                Need an Account ? <br/>
                <span className="line">
                    {/* buraya register router konulacak */}
                    <Link to='/register'> Sign up </Link>
                </span>
            </p>
        </section>
    )
}
export default Login;