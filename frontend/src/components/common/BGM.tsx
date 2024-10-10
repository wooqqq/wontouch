import { useEffect } from 'react';
import { useSelector } from 'react-redux';
import { RootState } from '../../redux/store';

const BackgroundMusic = ({ src }: { src: string }) => {
  const isMusicOn = useSelector((state: RootState) => state.music.isMusicOn);

  useEffect(() => {
    if (!isMusicOn) return; // 음악이 꺼져있으면 아무것도 하지 않음

    const audio = new Audio(src);
    audio.loop = true; // 반복 재생 설정
    audio.play(); // 음악 재생

    return () => {
      audio.pause(); // 페이지가 변경될 때 음악 중지
      audio.currentTime = 0; // 재생 위치 초기화
    };
  }, [isMusicOn, src]);

  return null; // UI에는 아무것도 표시하지 않음
};

export default BackgroundMusic;
