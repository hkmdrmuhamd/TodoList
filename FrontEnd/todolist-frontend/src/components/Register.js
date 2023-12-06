import { useRef, useState, useEffect } from 'react';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faCheck, faInfoCircle, faTimes } from '@fortawesome/free-solid-svg-icons';
import axios from '../api/axios';
import { Link } from 'react-router-dom';

const REGISTER_URL = '/register';

const Register = () => {
    const userRef = useRef();
    const errRef = useRef();

    const [userName, setUserName] = useState('');
    const [userFocus, setUserFocus] = useState(false);

    const [password, setPassword] = useState('');
    const [pwdFocus, setPwdFocus] = useState(false);

    const [matchPwd, setMatchPwd] = useState('');
    const [validMatch, setValidMatch] = useState(false);
    const [matchFocus, setMatchFocus] = useState(false);

    const [errMsg, setErrMsg] = useState('');
    const [success, setSuccess] = useState(false);

    useEffect(() => {
        userRef.current.focus();
    }, [])

    useEffect(() => {
        console.log(password);
        const match = password === matchPwd;
        setValidMatch(match);
    }, [password, matchPwd])

    useEffect(() => {
        setErrMsg('');
    }, [userName, password, matchPwd])

    const handleSubmit = async (e) => {
        e.preventDefault();
        <Link to='/login'></Link>

        try {
            const response = await axios.post(REGISTER_URL, JSON.stringify({ userName, password }), {
                // parantez içindeki user ve pwd back-end'den bu şekilde gelmeli !!! eğer user yerine username ve pwd yerine password şeklinde geliyor ise ; ({user:username , pwd: password}) olarak yazarız !!!!!
                headers: { 'Content-Type': 'application/json' },
                withCredentials: true
            });
            console.log(response.data);
            console.log(response.token);
            console.log(JSON.stringify(response));
            setSuccess(true);
        } catch (err) {
            if (!err?.response) {
                setErrMsg('no server');
            } else if (err.response?.status === 409) {
                setErrMsg('Username zaten var.');
            }
            else {
                setErrMsg('registration failed');
            }
            errRef.current.focus();
        }

        setSuccess(true);
    }
    return (
        <>
            {success
                ?
                <>
                    {alert('Success bir şekilde kayıt oldunuz !')}
                </>

                :
                <section>
                    <p ref={errRef} className={errMsg ? 'errmsg' : 'offscreen'} aria-live='assertive'>{errMsg}</p>
                    <h1>Register</h1>
                    <form onSubmit={handleSubmit}>
                        <label htmlFor='username'>Username:</label>
                        <input
                            id='username'
                            type='text'
                            ref={userRef}
                            autoComplete='off'
                            onChange={(e) => setUserName(e.target.value)}
                            required
                            aria-describedby='uidnote'
                            onFocus={() => setUserFocus(true)}
                            onBlur={() => setUserFocus(false)}
                        />
                        <p id='uidnote' className={userFocus && !userName ? 'instructions' : 'offscreen'}>
                            <FontAwesomeIcon icon={faInfoCircle} />
                            isim alanı boş bırakılamaz
                        </p>
                        <label htmlFor='password'>Password: </label>
                        <input
                            type='password'
                            id='password'
                            onChange={(e) => setPassword(e.target.value)}
                            required
                            arial-aria-describedby='pwdnote'
                            onFocus={() => setPwdFocus(true)}
                            onBlur={() => setPwdFocus(false)}
                        />
                        <p id='pwdnote' className={pwdFocus && !password ? 'instructions' : 'offscreen'}>
                            <FontAwesomeIcon icon={faInfoCircle} />
                            şifre boş bırakılamaz
                        </p>
                        <label htmlFor='confirm_pwd'>Confirm Password:
                            <span className={validMatch && matchPwd ? 'valid' : 'hide'}>
                                <FontAwesomeIcon icon={faCheck} />
                            </span>
                            <span className={validMatch || !matchPwd ? 'hide' : 'invalid'}>
                                <FontAwesomeIcon icon={faTimes} />
                            </span>
                        </label>
                        <input
                            id='confirm_pwd'
                            type='password'
                            onChange={(e) => setMatchPwd(e.target.value)}
                            required
                            aria-invalid={validMatch ? 'false' : 'true'}
                            aria-describedby='confirmnote'
                            onFocus={() => setMatchFocus(true)}
                            onBlur={() => setMatchFocus(false)}
                        />
                        <p id='confirmnote' className={matchFocus && !validMatch ? 'instructions' : 'offscreen'}>
                            <FontAwesomeIcon icon={faInfoCircle} />
                            Girdi alanı girilen şifre ile aynı olmak zorunda.
                        </p>

                        <button disabled={!userName || !password || !validMatch ? true : false}> SignUp </button>
                    </form>
                    <p>
                        Already registered ? <br />
                        {/* buraya router link koy */}
                        <Link to='/login'> Sign In</Link>
                    </p>
                </section>
            }
        </>
    )
}
export default Register;