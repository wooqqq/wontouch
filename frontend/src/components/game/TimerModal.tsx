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
  }, [dispatch, isPreparation]);  // isPreparation도 의존성 배열에 추가하여 대기시간/라운드 시간 전환 시 반영

  return (
    <div className="fixed top-0 left-0 w-full h-16 bg-black bg-opacity-80 z-50 flex justify-center items-center">
      <div className="text-white text-xl font-bold">
        {isPreparation ? (
          <p>대기시간: {preparationTime}초</p>  // 대기시간일 때
        ) : (
          <p>{round === 5 ? '마지막 라운드!' : `라운드 ${round}`} | 남은 시간: {duration}초</p>  // 라운드 시간일 때
        )}
      </div>
    </div>
  );
};

export default TimerModal;
