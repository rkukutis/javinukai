import { useNavigate } from "react-router-dom";
import expandMoreIcon from "../../assets/icons/expand_more_FILL0_wght400_GRAD0_opsz24.svg";
import expandLessIcon from "../../assets/icons/expand_less_FILL0_wght400_GRAD0_opsz24.svg";
import Button from "../Button";
import useUserStore from "../../stores/userStore";
import UserSubmissionView from "../UserSubmissionView";
import { useTranslation } from "react-i18next";
import deadlineReached from "../../utils/deadlineReached";

export function CategoryItem({
  categoryInfo,
  contestInfo,
  expandedCategory,
  onSetExpandedCategory,
  contestLimitReached,
  canParticipate,
}) {
  const { t } = useTranslation();
  const { user } = useUserStore((state) => state);
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
        <h1 className="text lg:text-lg">
          {categoryInfo.name} | {t(`categoryTypes.${categoryInfo.type}`)}
        </h1>
        <div className="flex space-x-4 items-center">
          <p className="text-nowrap">
            {t("CategoryItem.totalEntries")}: {categoryInfo.totalEntries}/
            {categoryInfo.maxTotalSubmissions}
          </p>
          <img src={isExpanded ? expandLessIcon : expandMoreIcon} />
        </div>
      </div>
      {isExpanded && (
        <div className="text border-2 border-t-white border-teal-400 rounded-b-md py-2 px-3 text-slate-700 leading-relaxed flex-col space-y-3">
          <p>{categoryInfo.description}</p>
          {user && (
            <div className=" flex xl:flex-row xl:space-x-4">
              {user.role != "USER" && (
                <Button
                  onClick={() =>
                    navigate(
                      `/contest/${contestInfo.id}/category/${categoryInfo.id}/contestant-entries`,
                      { state: { contestInfo, categoryInfo } }
                    )
                  }
                >
                  {t("CategoryItem.juryViewEntries")}
                </Button>
              )}
            </div>
          )}
          {user && canParticipate && (
            <UserSubmissionView
              contest={contestInfo}
              category={categoryInfo}
              contestLimitReached={
                contestLimitReached || deadlineReached(contestInfo.endDate)
              }
            />
          )}
        </div>
      )}
    </div>
  );
}
