// src/AppRouter.tsx
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import Lobby from './pages/Lobby';
import WaitingRoom from './pages/WaitingRoom';
import Game from './pages/Game';
import Login from './pages/Login';
import KakaoLoginHandler from './components/login/kakao/KakaoLoginHandler';
import KakaoToSignup from './components/login/kakao/KakaoToSignup';
import SignupWithKakao from './components/signup/kakao/SignupWithKakao';
import Setting from './pages/Setting';
// import Notfound from "./pages/Notfound";
import CommonBG from './components/common/CommonBG';

function AppRouter() {
  return (
    <Router>
      <Routes>
        {/* 로그인과 회원가입 관련 라우트 */}
        <Route path="/login" element={<Login />} />
        <Route path="/auth/kakao" element={<KakaoLoginHandler />} />
        <Route path="/signup" element={<KakaoToSignup />} />
        <Route path="/signup/kakao" element={<SignupWithKakao />} />

        {/* 공통 배경을 사용하는 라우트 */}
        <Route path="/*" element={<CommonBG />}>
          {/* 게임방 입장 전 대기 (게임방 목록 페이지) */}
          <Route path="lobby" element={<Lobby />} />
          {/* 게임 시작 전 대기방  */}
          <Route path="wait/:roomId" element={<WaitingRoom />} />
          <Route path="setting" element={<Setting />} />
          {/* <Route path="*" element={<Notfound />} /> */}
        </Route>

        {/* 게임 화면 */}
        <Route path="/game" element={<Game />} />
      </Routes>
    </Router>
  );
}

export default AppRouter;
