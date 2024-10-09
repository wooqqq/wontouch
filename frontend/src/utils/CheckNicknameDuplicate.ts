import axios from 'axios';

export const CheckNicknameDuplicate = async (
  nickname: string,
): Promise<boolean | null> => {
  const API_LINK = import.meta.env.VITE_API_URL;

  // 닉네임 미입력
  if (!nickname) {
    alert('닉네임을 입력해주세요.');
    return null;
  }

  // api로 중복체크
  try {
    const response = await axios.post(
      `${API_LINK}/user-profile/nickname/duplicate-check`,
      {
        nickname: nickname,
      },
    );
    return response.data.data ? false : true;
  } catch (error) {
    console.log('닉네임 중복 확인 불가', error);
    return null;
  }
};
