import UserUpdate from '../components/setting/UserUpdate';
import CharacterUpdate from '../components/setting/CharacterUpdate';
import { Route, Router, Routes } from 'react-router-dom';
import Header from '../components/common/Header';

function Setting() {
  return (
    <>
      <Header notificationCount={1} />
      <Routes>
        <Route path="profile" element={<UserUpdate />} />
        <Route path="character" element={<CharacterUpdate />} />
      </Routes>
    </>
  );
}

export default Setting;
