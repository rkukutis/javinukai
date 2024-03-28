import Button from "../Button";
import { useTranslation } from "react-i18next";
import addIcon from "../../assets/icons/add_circle_FILL0_wght400_GRAD0_opsz24.svg";

function CategoryPreview({ category, onAddCategory }) {
  const { t } = useTranslation();
  return (
    <div className="xl:grid xl:grid-cols-12 bg-white shadow py-2 px-3 place-items-left rounded">
      <h2 className="col-span-4 pr-3">{category.name}</h2>
      <p className="col-span-2">{(t(`categoryTypes.${category.type}`))}</p>
      <p className="col-span-2">{category.maxTotalSubmissions}</p>
      <p className="col-span-2">{category.maxUserSubmissions}</p>
      <div className="col-span-1 place-self-center">
        <Button onClick={() => onAddCategory(category)}>
          <img src={addIcon} />
        </Button>
      </div>
    </div>
  );
}

export default CategoryPreview;
