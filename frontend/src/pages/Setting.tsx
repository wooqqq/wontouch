import { Route, Routes } from "react-router-dom";
import UserUpdate from "../components/setting/UserUpdate";
import CharacterUpdate from "../components/setting/CharacterUpdate";

function Setting() {
  return (
    <>
      <div>Setting</div>
      <Routes>
        <Route path="userupdate" element={<UserUpdate />} />
        <Route path="characterupdate" element={<CharacterUpdate />} />
      </Routes>
    </>
  );
}

export default Setting;
