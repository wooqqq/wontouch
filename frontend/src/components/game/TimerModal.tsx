import React, { useEffect } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '../../redux/store';  // Redux 스토어 경로에 맞춰 수정하세요
import { decrementDuration, decrementPreparationTime } from '../../redux/slices/timeSlice';

const TimerModal: React.FC = () => {
  const { duration, round, preparationTime, isPreparation } = useSelector((state: RootState) => state.time);
  const dispatch = useDispatch();

  useEffect(() => {
    const timer = setInterval(() => {
      if (isPreparation) {
        dispatch(decrementPreparationTime());  // 대기시간 감소
      } else {
        dispatch(decrementDuration());  // 라운드 시간 감소
      }
    }, 1000); // 1초마다 실행

    // 컴포넌트가 언마운트되거나 업데이트될 때 타이머를 정리해줍니다.
    return () => clearInterval(timer);
  }, [dispatch, isPreparation]);

  // 시간을 "00분 00초" 형식으로 변환하는 함수
  const formatTime = (totalSeconds: number) => {
    const minutes = Math.floor(totalSeconds / 60);
    const seconds = totalSeconds % 60;

    // 분이 있으면 "00분 00초", 없으면 "00초"로 표시
    return minutes > 0
      ? `${String(minutes).padStart(2, '0')}분 ${String(seconds).padStart(2, '0')}초`
      : `${String(seconds).padStart(2, '0')}초`;
  };

  return (
    <div className="fixed top-0 left-0 w-full h-16 bg-black bg-opacity-80 z-50 flex justify-center items-center">
      <div className="text-white text-xl font-bold">
        {isPreparation ? (
          <p>다음 라운드까지 : {formatTime(preparationTime)}</p>  // 대기시간일 때
        ) : (
          <p>{round === 5 ? <span className='text-red-500'>마지막 라운드!</span> : `라운드 ${round}`} {' | '}
            <span className={duration < 30 ? 'text-red-600' : 'text-yellow-600'}>
              남은 시간: {formatTime(duration)}
            </span>
          </p>  // 라운드 시간일 때
        )}
      </div>
    </div>
  );
};

export default TimerModal;
