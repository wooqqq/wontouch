import { useNavigate } from 'react-router-dom';

import arrow from '../../assets/icon/arrow_left.png';

export default function BackButton() {
  const navigate = useNavigate();

  const clickButton = () => {
    navigate('/lobby');
  };
  return (
    <div>
      <button onClick={clickButton}>
        <img src={arrow} alt="" />
      </button>
    </div>
  );
}
