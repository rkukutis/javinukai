import { Outlet, useNavigate } from "react-router-dom";
import expandMoreIcon from "../../assets/icons/expand_more_FILL0_wght400_GRAD0_opsz24.svg";
import expandLessIcon from "../../assets/icons/expand_less_FILL0_wght400_GRAD0_opsz24.svg";
import Button from "../Button";

export function CategoryItem({
  categoryInfo,
  contestId,
  expandedCategory,
  onSetExpandedCategory,
}) {
  const navigate = useNavigate();
  const isExpanded = categoryInfo.id == expandedCategory;

  function handleExpandClick() {
    if (isExpanded) {
      onSetExpandedCategory("");
    } else {
      onSetExpandedCategory(categoryInfo.id);
    }
  }

  return (
    <div className="w-full rounded-md">
      <div
        onClick={handleExpandClick}
        className={`flex hover:cursor-pointer justify-between px-3 py-2 w-full ${
          isExpanded
            ? "rounded-t-md bg-teal-400 text-teal-800"
            : "rounded-md bg-slate-50 text-slate-700"
        }`}
      >
        <h1 className="text text-xl">
          {categoryInfo.name} - {categoryInfo.type}
        </h1>
        <img src={isExpanded ? expandLessIcon : expandMoreIcon} />
      </div>
      {isExpanded && (
        <div className="text border-2 border-t-white border-teal-400 rounded-b-md py-2 px-3 text-slate-700 leading-relaxed flex-col space-y-3">
          <p>{categoryInfo.description}</p>
          <div className=" flex xl:flex-row xl:space-x-4">
            <Button
              onClick={() => navigate(`category/${categoryInfo.id}/upload`)}
            >
              Add photos to category
            </Button>
            <Button
              onClick={() => navigate(`category/${categoryInfo.id}/my-entries`)}
            >
              View my submissions
            </Button>
            <Button
              onClick={() =>
                navigate(
                  `/contest/${contestId}/category/${categoryInfo.id}/contestant-entries`
                )
              }
            >
              View contestant photos
            </Button>
          </div>
          <Outlet context={{ contestId, categoryId: categoryInfo.id }} />
        </div>
      )}
    </div>
  );
}
