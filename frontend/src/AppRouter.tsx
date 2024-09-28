import {
  BrowserRouter as Router,
  Routes,
  Route,
  Navigate,
  useNavigate,
} from "react-router-dom";
import { useEffect } from "react";
import Lobby from "./pages/Lobby";
import WaitingRoom from "./pages/WaitingRoom";
import Game from "./pages/Game";
import Login from "./pages/Login";
import KakaoLoginHandler from "./components/login/kakao/KakaoLoginHandler";
import KakaoToSignup from "./components/login/kakao/KakaoToSignup";
import SignupWithKakao from "./components/signup/kakao/SignupWithKakao";
import Setting from "./pages/Setting";
import CommonBG from "./components/common/CommonBG";

// 로그인이 되어있지 않을 때, 다른 페이지로 이동하려고 하면 강제로 로그인 창으로 이동
// children은 렌더링 될 컴포넌트
function ProtectedRoute({ children }: { children: JSX.Element }) {
  const navigate = useNavigate();

  useEffect(() => {
    const token = localStorage.getItem("access_token");
    if (!token) {
      navigate("/login");
    }
  }, [navigate]);

  // 로그인 되어있는 상태라면 해당 컴포넌트 return
  return children;
}

function AppRouter() {
  return (
    <Router>
      <Routes>
        {/* 루트 경로가 로그인 상태를 확인하도록 설정 */}
        <Route
          path="/"
          element={
            <ProtectedRoute>
              <Navigate to="/login" />
            </ProtectedRoute>
          }
        />

        {/* 로그인과 회원가입 관련 라우트 */}
        <Route path="/login" element={<Login />} />
        <Route path="/auth/kakao" element={<KakaoLoginHandler />} />
        <Route path="/signup" element={<KakaoToSignup />} />
        <Route path="/signup/kakao" element={<SignupWithKakao />} />

        {/* 공통 배경을 사용하는 라우트 */}
        <Route path="/*" element={<CommonBG />}>
          {/* ProtectedRoute로 감싸서 토큰이 없으면 로그인으로 리디렉션 */}
          <Route
            path="lobby"
            element={
              <ProtectedRoute>
                <Lobby />
              </ProtectedRoute>
            }
          />
          <Route
            path="waiting-room/:roomId"
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

        {/* 게임 화면 */}
        <Route
          path="/game"
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
