import { useState } from "react";
import Modal from "../Modal";
import Button from "../Button";
import deleteIcon from "../../assets/icons/delete_FILL0_wght400_GRAD0_opsz24.svg";
import editIcon from "../../assets/icons/edit_FILL0_wght400_GRAD0_opsz24.svg";
import EditCategoryModal from "./EditCategoryModal";
import { useTranslation } from "react-i18next";

export function SelectedCategoryItem({
  category,
  onRemoveCategory,
  onUpdateCategory,
}) {
  const [editModalIsOpen, setEditModalIsOpen] = useState(false);
  const { t } = useTranslation();

  return (
    <div className="xl:grid xl:grid-cols-12 bg-white p-2 rounded shadow">
      <h2 className="col-span-4 self-center">{category.name}</h2>
      <h2 className="col-span-2 self-center">{(t(`categoryTypes.${category.type}`))}</h2>
      <h2 className="col-span-2 self-center">{category.maxTotalSubmissions}</h2>
      <h2 className="col-span-2 self-center">{category.maxUserSubmissions}</h2>
      <div className="col-span-2 space-x-2 self-center flex justify-end">
        <Button
          type="button"
          onClick={() => setEditModalIsOpen(true)}
          extraStyle="bg-yellow-500 hover:bg-yellow-400"
        >
          <img src={editIcon} />
        </Button>
        <Button
          type="button"
          extraStyle="bg-red-500 hover:bg-red-400"
          onClick={() => onRemoveCategory(category)}
        >
          <img src={deleteIcon} />
        </Button>
      </div>
      <Modal isOpen={editModalIsOpen} setIsOpen={setEditModalIsOpen}>
        <EditCategoryModal
          onUpdateCategory={onUpdateCategory}
          category={category}
          onClose={() => setEditModalIsOpen(false)}
        />
      </Modal>
    </div>
  );
}
