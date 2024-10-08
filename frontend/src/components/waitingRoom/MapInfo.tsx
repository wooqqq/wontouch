import map from '../../assets/map/map.png';

function MapInfo() {
  return (
    <div className="yellow-box pb-5 mt-7">
      <div className="mint-title my-1">맵</div>
      <div>
        <img
          src={map}
          alt="맵 미리보기"
          className="w-[300px] mx-auto my-0 rounded-[10px]"
        />
      </div>
    </div>
  );
}

export default MapInfo;
