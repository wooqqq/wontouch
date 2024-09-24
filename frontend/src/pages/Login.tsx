import React, { useState } from "react";

import GoogleLogin from "../components/Login/Google/GoogleLogin";
import KakaoLogin from "../components/Login/Kakao/KakaoLogin";
import LoginButton from "../components/Login/LoginButton";

function Login() {
  // useState로 로그인 버튼을 숨기고 로그인 옵션 보여주기
  const [showLoginOptions, setShowLoginOptions] = useState(false);

  const handleLoginClick = () => {
    setShowLoginOptions(true);
  };

  return (
    <div>
      {/* <img src="src/assets/tmp.png" alt="임시 로그인 배경 화면" /> */}
      <div className="mb-20">
        <img src="src/assets/Login/logo.gif" alt="로고" />
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
