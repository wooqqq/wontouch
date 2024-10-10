import { useNavigate } from 'react-router-dom';

function KakaoToSignup() {
  const navigate = useNavigate();

  const goToSignup = async () => {
    navigate('/signup/kakao');
  };

  return (
    <div className="pt-52">
      <div className="text-2xl mb-8 text-center">캐릭터가 없습니다!</div>
      <div className="text-5xl mb-8 text-center mint-title">
        캐릭터 생성하러 가볼까요?
      </div>
      <div className="ready-button w-2/5 mx-auto">
        <button onClick={goToSignup}>캐릭터 생성</button>
      </div>
    </div>
  );
}

export default KakaoToSignup;
