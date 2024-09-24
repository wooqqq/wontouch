// src/AppRouter.tsx
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import Lobby from "./pages/Lobby";
import WaitingRoom from "./pages/WaitingRoom";
import Game from "./pages/Game";
import Login from "./pages/Login";
import KakaoLoginHandler from "./components/Login/Kakao/KakaoLoginHandler";
import SignupWithKakao from "./components/Login/Kakao/SignupWithKakao";

function AppRouter() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<Login />} />
        <Route path="/auth/kakao" element={<KakaoLoginHandler />} />
        <Route path="/signup/kakao" element={<SignupWithKakao />} />
        {/* 게임방 입장 전 대기 (게임방 목록 페이지) */}
        <Route path="/lobby" element={<Lobby />} />
        {/* 게임 시작 전 대기방 */}
        <Route path="/waiting-room" element={<WaitingRoom />} />
        <Route path="/game" element={<Game />} />
      </Routes>
    </Router>
  );
}

export default AppRouter;
