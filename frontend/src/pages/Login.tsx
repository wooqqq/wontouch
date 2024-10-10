import { useState } from 'react';
import wontouchLogo from '../assets/login/logo.gif';
import BackgroundMusic from '../components/common/BackGroundMusic';
import KakaoLogin from '../components/login/kakao/KakaoLogin';
import LoginButton from '../components/login/LoginButton';

function Login() {
  const [showLoginOptions, setShowLoginOptions] = useState(false);
  const [isMusicPlaying, setIsMusicPlaying] = useState(false); // 음악 재생 상태 관리

  const handleLoginClick = () => {
    setShowLoginOptions(true);
    setIsMusicPlaying(true); // 음악 재생 시작
  };

  return (
    <div className="flex flex-col items-center h-screen justify-center">
      <BackgroundMusic />
      <div className="mb-24">
        <img src={wontouchLogo} alt="원터치 게임 로고" />
      </div>
      <div className="min-h-[100px]">
        {!showLoginOptions ? (
          <LoginButton onClick={handleLoginClick} />
        ) : (
          <div className="flex flex-col items-center space-y-4">
            <KakaoLogin />
          </div>
        )}
      </div>
    </div>
  );
}

export default Login;
