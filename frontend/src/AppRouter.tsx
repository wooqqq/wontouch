import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from 'react-router-dom';
import { useEffect } from 'react';
import Lobby from './pages/Lobby';
import WaitingRoom from './pages/WaitingRoom';
import Game from './pages/Game';
import Login from './pages/Login';
import KakaoLoginHandler from './components/login/kakao/KakaoLoginHandler';
import KakaoToSignup from './components/login/kakao/KakaoToSignup';
import SignupWithKakao from './components/signup/kakao/SignupWithKakao';
import CommonBG from './components/common/CommonBG';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from './redux/store';
import { setToken } from './redux/slices/authSlice';
import { setUserId } from './redux/slices/userSlice';
import { jwtDecode } from 'jwt-decode';
import Edit from './pages/Edit';

interface DecodedToken {
  userId: number;
}

// 로그인 하지 않은 사용자의 다른 페이지 접근 방지
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

        <Route path="/*" element={<CommonBG />}>
          <Route path="auth/kakao" element={<KakaoLoginHandler />} />
          <Route
            path="signup"
            element={
              <ProtectedRoute>
                <KakaoToSignup />
              </ProtectedRoute>
            }
          />
          <Route
            path="signup/kakao"
            element={
              <ProtectedRoute>
                <SignupWithKakao />
              </ProtectedRoute>
            }
          />
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
            path="edit/*"
            element={
              <ProtectedRoute>
                <Edit />
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
    </Router>
  );
}

export default AppRouter;
