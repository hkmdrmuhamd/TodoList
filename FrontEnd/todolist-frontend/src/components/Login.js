import { useRef, useState, useEffect, useContext } from "react";
import AuthContext from "../context/AuthProvider";
import axios from "../api/axios";
import { Link, Route } from "react-router-dom";
import Show from "./Show";
const LOGIN_URL = '/auth';

const Login = () => {
    const { setAuth } = useContext(AuthContext);
    const userRef = useRef();
    const errRef = useRef();

    const [user, setUser] = useState('');
    const [pwd, setPwd] = useState('');
    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false); // bunun yerine router kullanacağız !! bu şimdilik çalışıp çalışmadığını anlamak için kullandığımız bir yöntem. 

    useEffect(() => {
        userRef.current.focus();
    }, [])

    useEffect(() => {
        setErrMsg('');
    }, [user, pwd])

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await axios.post(
                LOGIN_URL,
                JSON.stringify({ user, pwd }),
                {
                    headers: { 'Content-Type': 'application/json' },
                    withCredentials: true
                }
            );
            // eğer back-end'den user değil de username ve pwd değil password geliyor ise; username:user, password:pwd şeklinde yazılır.
            console.log(JSON.stringify(response?.data));
            //console.log(JSON.stringify(response));
            const accessToken = response?.data?.accessToken;
            const roles = response?.data?.roles;
            setAuth({ user, pwd, roles, accessToken });
            setUser('');
            setPwd('');
            setSuccess(true);
        } catch (err) {
            if (!err?.response) {
                setErrMsg('no server response');
            } else if (err.response?.status === 400) {
                setErrMsg('missing username or password');
            } else if (err.response?.status === 401) {
                setErrMsg('unauthorized');
            }
            else {
                setErrMsg('login failed')
            }
            errRef.current.focus();
        }
    }

    return (
        <>
            {success
                ? (
                    <Route path='/show' element={<Show />} />

                ) : (
                    <section>
                        <p ref={errRef} className={errMsg ? 'errmsg' : 'offscreen'} aria-live="assertive">
                            {errMsg}
                        </p >
                        <h1>Sign In</h1>
                        <form onSubmit={handleSubmit} >
                            <label htmlFor="username">Username: </label>
                            <input
                                id="username"
                                type="text"
                                ref={userRef}
                                onChange={(e) => setUser(e.target.value)}
                                value={user}
                                required
                            />

                            <label htmlFor="password">Password: </label>
                            <input
                                id="password"
                                type="password"
                                onChange={(e) => setPwd(e.target.value)}
                                value={pwd}
                                required
                            />
                            <button>Sign In </button>
                        </form>
                        <p>
                            Need an Account ? <br />
                            <span className="line">
                                {/* buraya register router konulacak */}
                                <Link to='/'> Sign up </Link>
                            </span>
                        </p>
                    </section >
                )
            }
        </>
    )
}
export default Login;