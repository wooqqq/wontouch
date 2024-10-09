import { useNavigate } from 'react-router-dom';

function KakaoToSignup() {
  const navigate = useNavigate();

  const goToSignup = async () => {
    navigate('/signup/kakao');
  };

  return (
    <div className="flex flex-col items-center justify-center h-screen">
      <div className="text-5xl mb-8">캐릭터 생성하러 가볼까요?</div>
      <button className="ready-button w-3/12 h-[80px]" onClick={goToSignup}>
        캐릭터 생성
      </button>
    </div>
  );
}

export default KakaoToSignup;
