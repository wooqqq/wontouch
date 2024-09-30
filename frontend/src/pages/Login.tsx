import React, { useState } from 'react';

import GoogleLogin from '../components/login/google/GoogleLogin';
import KakaoLogin from '../components/login/kakao/KakaoLogin';
import LoginButton from '../components/login/LoginButton';

function Login() {
  // useState로 로그인 버튼을 숨기고 로그인 옵션 보여주기
  const [showLoginOptions, setShowLoginOptions] = useState(false);

  const handleLoginClick = () => {
    setShowLoginOptions(true);
  };

  return (
    <div className="flex flex-col items-center justify-center min-h-screen">
      <div className="mb-20">
        <img src="src/assets/login/logo.gif" alt="로고" />
      </div>
      {!showLoginOptions ? (
        <LoginButton onClick={handleLoginClick} />
      ) : (
        <div className="flex flex-col items-center space-y-4">
          <KakaoLogin />
          <GoogleLogin />
        </div>
      )}
    </div>
  );
}

export default Login;
