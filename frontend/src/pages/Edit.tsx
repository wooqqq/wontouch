import EditProfile from '../components/edit/EditProfile';
import EditCharacter from '../components/edit/EditCharacter';
import { Route, Routes, useNavigate } from 'react-router-dom';
import Header from '../components/common/Header';

import arrow from '../assets/icon/arrow_left.png';

function Edit() {
  const navigate = useNavigate();

  // 뒤로 가기
  const clickArrow = () => {
    navigate('/lobby');
  };
  return (
    <>
      <div className="flex items-center justify-between">
        <button onClick={clickArrow}>
          <img src={arrow} alt="뒤로가기" />
        </button>
        <div>
          <Header />
        </div>
      </div>
      <Routes>
        <Route path="profile" element={<EditProfile />} />
        <Route path="character" element={<EditCharacter />} />
      </Routes>
    </>
  );
}

export default Edit;
