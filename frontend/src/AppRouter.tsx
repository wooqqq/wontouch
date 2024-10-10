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

// 로그인 하지 않은 사용자의 접근 방지
function ProtectedRoute({ children }: { children: JSX.Element }) {
  const navigate = useNavigate();
  const token = sessionStorage.getItem('access_token');

  useEffect(() => {
    if (!token) {
      navigate('/login');
    }
  }, [token, navigate]);

  return children;
}

// 로그인된 사용자가 로그인 페이지에 접근하지 못하게
function AuthRoute({ children }: { children: JSX.Element }) {
  const token = sessionStorage.getItem('access_token');
  if (token) {
    return <Navigate to="/lobby" />;
  }
  return children;
}

// 닉네임이 등록된 사용자가 가입 페이지에 접근하지 못하게
function SignupProtectedRoute({ children }: { children: JSX.Element }) {
  const userNickname = useSelector((state: RootState) => state.user.nickname);
  if (userNickname) {
    return <Navigate to="/lobby" />;
  }
  return children;
}

function AppRouter() {
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

        <Route path="/*" element={<CommonBG />}>
          {/* 로그인 화면 접근 방지 */}
          <Route
            path="login"
            element={
              <AuthRoute>
                <Login />
              </AuthRoute>
            }
          />
          <Route
            path="auth/kakao"
            element={
              <AuthRoute>
                <KakaoLoginHandler />
              </AuthRoute>
            }
          />

          {/* 가입 화면 접근 방지 */}
          <Route
            path="signup"
            element={
              <SignupProtectedRoute>
                <KakaoToSignup />
              </SignupProtectedRoute>
            }
          />
          <Route
            path="signup/kakao"
            element={
              <SignupProtectedRoute>
                <SignupWithKakao />
              </SignupProtectedRoute>
            }
          />

          {/* 보호된 라우트 */}
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
