import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';

import boy from '../../../assets/background/characters/stand/boy.png';

const API_LINK = import.meta.env.VITE_API_URL;

function SignupWithKakao() {
  const navigate = useNavigate();

  const [nickname, setNickname] = useState('');
  const [isNicknameAvailable, setIsNicknameAvailable] = useState<
    boolean | null
  >(null);

  const handleCheckNickname = async () => {
    if (!nickname) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    // 닉네임 중복 체크
    try {
      const response = await axios.post(
        `${API_LINK}/user-profile/nickname/duplicate-check`,
        {
          nickname: nickname,
        },
      );

      // response.data.data가 false로 나와야 닉네임 중복을 피함.
      if (response.data.data) {
        setIsNicknameAvailable(false);
      } else {
        setIsNicknameAvailable(true);
      }
    } catch (error) {
      console.log('닉네임 중복 확인 불가', error);
    }
  };

  // store에 저장된 userId와 입력받은 nickname을 이용해서 회원가입 진행
  const userId = useSelector((state: RootState) => state.user.id);

  const signupWithKakao = async () => {
    event?.preventDefault(); // 새로고침 방지

    if (!nickname) {
      alert('닉네임을 입력해주세요.');
      return;
    }

    const isKorean = /^[가-힣]*$/.test(nickname); // 한글만
    const isEnglish = /[a-zA-Z]*$/.test(nickname); // 영어만
    const isValidNickname = /^[가-힣a-zA-Z]*$/.test(nickname); // 한글, 영어 혼용

    console.log(nickname);

    // 유효성 검사
    if (isKorean && nickname.length > 6) {
      alert('한글은 최대 6자까지 입력할 수 있습니다.');
      return;
    } else if (isEnglish && nickname.length > 10) {
      alert('영어는 최대 10자까지 입력할 수 있습니다.');
      return;
    } else if (isValidNickname && nickname.length > 10) {
      alert('한글, 영어 혼합은 최대 10자까지 입력할 수 있습니다.');
      return;
    }

    if (isNicknameAvailable === false) {
      alert('중복된 닉네임입니다. 다른 닉네임을 입력해주세요.');
      return;
    }

    try {
      await axios.post(`${API_LINK}/user-profile/join`, {
        userId: userId,
        nickname: nickname,
      });
      navigate('/');
    } catch (error) {
      console.error('회원가입 중 오류 발생:', error);
      alert('회원가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
    }
  };

  return (
    <div className="flex flex-col items-center">
      <div className="mint-title text-5xl mb-8">캐릭터생성</div>
      <div className="yellow-box flex flex-col items-center w-[800px] h-[480px]">
        <div className="profile-img w-[230px] h-[230px] flex items-center justify-center p-4">
          <img
            className="w-full h-full object-contain"
            src={boy}
            alt="기본 캐릭터"
          />
        </div>

        <form onSubmit={signupWithKakao}>
          <div className="flex items-center justify-between mt-6">
            <label htmlFor="nickname" className="text-[#10AB7D] text-3xl mr-8">
              닉네임
            </label>
            <input
              className="font-['Galmuri11'] w-[420px] mr-6 p-3 signup-input"
              id="nickname"
              placeholder="한글 6자, 영어 10자, 혼용 10자 제한"
              value={nickname}
              onChange={(event) => setNickname(event.target.value)}
            />
            <button
              type="button"
              className="ready-button w-[130px] h-[15px] text-2xl border-[#10AB7D] border-4"
              onClick={handleCheckNickname}
            >
              중복확인
            </button>
          </div>

          <div className="text-left ml-32 font-['Galmuri11'] min-h-[24px]">
            {isNicknameAvailable === true && (
              <p className="text-green-600">사용 가능한 닉네임입니다.</p>
            )}
            {isNicknameAvailable === false && (
              <p className="text-red-600">이미 사용 중인 닉네임입니다.</p>
            )}
          </div>

          <div className="flex justify-center mt-6">
            <button
              type="submit"
              className="ready-button w-[200px] h-[30px] text-3xl border-[#10AB7D] border-4"
            >
              생성
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

export default SignupWithKakao;
