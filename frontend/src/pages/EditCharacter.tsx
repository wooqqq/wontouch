import Header from '../components/common/Header';
import { useNavigate } from 'react-router-dom';
import { postUserMileage } from '../redux/slices/userSlice';
import { useDispatch } from 'react-redux';

import arrow from '../assets/icon/arrow_left.png';

export default function EditCharacter() {
  const navigate = useNavigate();

  // 뒤로 가기
  const clickArrow = () => {
    navigate('/lobby');
  };

  // 캐릭터 구입 시, 마일리지 차감은
  // userSlice에 구현해둔 postUserMileage를 사용하시면 됩니당
  // dispatch(postUserMileage(10000))
  // 이 예시는 10000원짜리 캐릭터를 사는 로직입니다!!
  // store에 바로 값을 반영해줘야 새로고침 없이 화면에 리렌더링 됨!!

  return (
    <div>
      <div className="flex items-center justify-between">
        <button onClick={clickArrow}>
          <img src={arrow} alt="" />
        </button>
        <div>
          <Header />
        </div>
      </div>
    </div>
  );
}
