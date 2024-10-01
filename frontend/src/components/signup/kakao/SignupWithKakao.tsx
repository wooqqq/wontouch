import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { RootState } from '../../../redux/store';

import boy from '../../../assets/background/characters/stand/boy.png';
import './SignupWithKakao.css';

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
    if (isNicknameAvailable === false) {
      alert('중복된 닉네임입니다. 다른 닉네임을 입력해주세요.');
      return;
    }

    console.log(userId);
    console.log(nickname);

    try {
      const res = await axios.post(`${API_LINK}/user-profile/join`, {
        userId: userId,
        nickname: nickname,
      });
      console.log(res.data);
      navigate('/');
    } catch (error) {
      console.error('회원가입 중 오류 발생:', error);
      alert('회원가입 중 오류가 발생했습니다. 나중에 다시 시도해주세요.');
    }
  };

  return (
    <div className="flex flex-col items-center">
      <div className="mint-title">캐릭터생성</div>
      <div className="yellow-box flex flex-col items-center w-[800px] h-[480px]">
        <div className="profile-img w-[200px] h-[200px] flex items-center justify-center p-4">
          <img
            className="w-full h-full object-contain"
            src={boy}
            alt="기본 캐릭터"
          />
        </div>

        <div className="w-full flex flex-col items-center">
          <form onSubmit={signupWithKakao} className="w-full max-w-lg">
            <div className="flex items-center justify-center space-x-4 mb-2">
              <label htmlFor="nickname" className="text-green-700 text-2xl">
                닉네임
              </label>
              <input
                className="font-['Galmuri11'] w-[400px]"
                id="nickname"
                placeholder="한글, 숫자, 영문 입력 가능 (한글 기준 최대 6자)"
                value={nickname}
                onChange={(event) => setNickname(event.target.value)}
              />
              <button
                type="button"
                className="ready-button w-[130px] h-[30px] text-2xl"
                onClick={handleCheckNickname}
              >
                중복확인
              </button>
            </div>

            <div className="text-left ml-4 font-['Galmuri11'] mb-4">
              {isNicknameAvailable === true && (
                <p className="text-green-600">사용 가능한 닉네임입니다.</p>
              )}
              {isNicknameAvailable === false && (
                <p className="text-red-600">이미 사용 중인 닉네임입니다.</p>
              )}
            </div>

            <div className="flex justify-center">
              <button
                type="submit"
                className="ready-button w-[180px] h-[50px] text-3xl"
              >
                생성
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
}

export default SignupWithKakao;
