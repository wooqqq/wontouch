import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
  useLocation,
} from 'react-router-dom';
import { useEffect } from 'react';
import Lobby from './pages/Lobby';
import WaitingRoom from './pages/WaitingRoom';
import Game from './pages/Game';
import Login from './pages/Login';
import KakaoLoginHandler from './components/login/kakao/KakaoLoginHandler';
import KakaoToSignup from './components/login/kakao/KakaoToSignup';
import SignupWithKakao from './components/signup/kakao/SignupWithKakao';
import Setting from './pages/Setting';
import CommonBG from './components/common/CommonBG';
import { useDispatch } from 'react-redux';
import { setToken } from './redux/slices/authSlice';
import { setUserId } from './redux/slices/userSlice';
import { jwtDecode } from 'jwt-decode';

interface DecodedToken {
  userId: number;
}

function ProtectedRoute({ children }: { children: JSX.Element }) {
  const navigate = useNavigate();
  const token = localStorage.getItem('access_token');

  useEffect(() => {
    if (!token) {
      navigate('/login');
    }
  }, [token, navigate]);

  return children;
}

function AppRouter() {
  const dispatch = useDispatch();
  const token = localStorage.getItem('access_token');

  useEffect(() => {
    if (token) {
      dispatch(setToken(token));
      const decodedToken = jwtDecode<DecodedToken>(token);
      dispatch(setUserId(decodedToken.userId));
    }
  }, [token, dispatch]);

  return (
    <Router>
      <Content />
    </Router>
  );
}

function Content() {
  const location = useLocation();
  const isGame = location.pathname.startsWith('/game');

  return (
    <>
      {!isGame && <Header />}
      <Routes>
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Navigate to="/login" />
            </ProtectedRoute>
          }
        />

        <Route path="/login" element={<Login />} />
        <Route path="/auth/kakao" element={<KakaoLoginHandler />} />
        <Route path="/signup" element={<KakaoToSignup />} />
        <Route path="/signup/kakao" element={<SignupWithKakao />} />

        <Route path="/*" element={<CommonBG />}>
          <Route
            path="lobby"
            element={
              <ProtectedRoute>
                <Lobby />
              </ProtectedRoute>
            }
          />
          <Route
            path="wait/:roomId"
            element={
              <ProtectedRoute>
                <WaitingRoom />
              </ProtectedRoute>
            }
          />
          <Route
            path="setting"
            element={
              <ProtectedRoute>
                <Setting />
              </ProtectedRoute>
            }
          />
        </Route>

        <Route
          path="/game/:roomId"
          element={
            <ProtectedRoute>
              <Game />
            </ProtectedRoute>
          }
        />
      </Routes>
    </>
  );
}

export default AppRouter;
