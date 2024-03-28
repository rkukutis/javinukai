import { useNavigate } from "react-router-dom";
import contestPhoto from "../../assets/contest-photo.jpg";
import calendarIcon from "../../assets/icons/date_range_FILL0_wght400_GRAD0_opsz24.svg";
import formatTimestap from "../../utils/formatTimestap";
import Button from "../Button";
import { useTranslation } from "react-i18next";
import useUserStore from "../../stores/userStore";

export default function ContestCard({ contestInfo }) {
  const { user } = useUserStore((state) => state);
  const navigate = useNavigate();
  const { t } = useTranslation();

  return (
    <div className="bg-white rounded-md xl:grid xl:grid-cols-12">
      <img
        className="h-[15rem] w-full object-cover shadow-md xl:col-span-2 rounded-t-md xl:rounded-t-none xl:rounded-l"
        src={contestInfo.thumbnailURL ?? contestPhoto}
      />
      <div className="text xl:col-span-10 p-6 space-y-3">
        <h1 className="text-2xl font-semibold text-teal-500 over">
          {contestInfo.name}
        </h1>
        <p className="text overflow-clip h-24 bg-gradient-to-b text-slate-700">
          {contestInfo.description}
        </p>
        <div className="flex flex-col xl:flex-row space-y-2 lg:space-y-0 items-center xl:space-x-2 w-">
          <Button
            onClick={() =>
              user
                ? navigate(`/contest/${contestInfo.id}`, {
                    withCredentials: true,
                  })
                : navigate(`/login`)
            }
            extraStyle="w-full xl:w-fit"
          >
            {t("ContestCard.detailsButton")}
          </Button>

          <div className="flex items-center space-x-2">
            <img className="inline" src={calendarIcon} />
            <p className="text-slate-600 font-semibold">
              {formatTimestap(contestInfo.startDate)} -{" "}
              {formatTimestap(contestInfo.endDate)}
            </p>
          </div>
        </div>
      </div>
    </div>
  );
}
