import Modal from './Modal';

import confirm from '../../assets/icon/confirm.png';
import alterd from '../../assets/icon/expression_alerted.png';

export default function AlertModal({
  message,
  closeAlterModal,
}: {
  message: string;
  closeAlterModal: () => void;
}) {
  return (
    <Modal>
      <div className="yellow-box w-2/5 h-[180px] border-[#36EAB5] bg-[#FFFEEE] p-8">
        <div className="flex white-text text-4xl mb-10 justify-center">
          <img src={alterd} alt="" className="mr-4" />
          <div>{message}</div>
        </div>
        <button>
          <img src={confirm} alt="" />
        </button>
      </div>
    </Modal>
  );
}
