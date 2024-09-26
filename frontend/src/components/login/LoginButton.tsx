import React from "react";

interface LoginButtonProps {
  onClick: React.MouseEventHandler<HTMLButtonElement>;
}

const LoginButton: React.FC<LoginButtonProps> = ({ onClick }) => {
  return <button onClick={onClick}>게임 시작</button>;
};

export default LoginButton;
