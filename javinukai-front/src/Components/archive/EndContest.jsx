import { useState } from "react";
import PaginationSettings from "../PaginationSettings";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import getCompetingUsers from "../../services/users/getCompetingUsers";
import Button from "../Button";
import endCompetition from "../../services/archive/endCompetition";
import toast from "react-hot-toast";
import ParticipantCard from "./ParticipantCard";
import SpinnerPage from "../../pages/SpinnerPage";
import ChangePage from "../user-management/ChangePage";

const defaultPagination = {
  page: 0,
  limit: 25,
  sortBy: "email",
  sortDesc: "false",
  searchedField: null,
};

function EndContest({ contest, close }) {
  const { id } = contest;
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const [winners, setWinners] = useState([]);

  const { data, isFetching } = useQuery({
    queryKey: [
      "stillCompeting",
      id,
      paginationSettings.page,
      paginationSettings.limit,
      paginationSettings.sortBy,
      paginationSettings.sortDesc,
      paginationSettings.searchedField,
    ],
    queryFn: () =>
      getCompetingUsers(
        id,
        paginationSettings.page,
        paginationSettings.limit,
        paginationSettings.sortBy,
        paginationSettings.sortDesc,
        paginationSettings.searchedField
      ),
  });
  const { t } = useTranslation();

  const handleClick = (email) => {
    if (winners.includes(email)) {
      const filteredWinners = winners.filter((winner) => winner !== email);
      setWinners(filteredWinners);
      return;
    }
    setWinners([...winners, email]);
  };

  const handleEndContest = () => {
    mutate({ id, winners });
    close();
  };

  const { mutate } = useMutation({
    mutationFn: (data) => endCompetition(data),
    onSuccess: () => {
      toast.success("konkursas užbaigtas");
    },
    onError: (err) => {
      toast.error(err.message);
    },
  });

  const isWinner = (participant) => winners.includes(participant.email);

  const displayParticipants = data?.content.map((participant) => {
    return (
      <ParticipantCard
        key={participant.email}
        handleClick={handleClick}
        participant={participant}
        isWinner={isWinner}
      />
    );
  });

  return (
    <>
      {isFetching ? (
        <SpinnerPage />
      ) : (
        <div className="w-full min-h-[82vh] xl:flex xl:flex-col xl:items-center bg-slate-50">
          <div className="xl:w-4/4 w-full px-2">
            <PaginationSettings
              pagination={paginationSettings}
              setPagination={setPaginationSettings}
              availablePageNumber={data?.totalPages}
              limitObjectName={t("UserManagementPage.userLimitObject")}
              sortFieldOptions={
                <>
                  <option value="contestName">
                    {t("ArchivePage.contestName")}
                  </option>
                </>
              }
              searchByFieldName={t("ArchivePage.contestName")}
              firstPage={data?.first}
              lastPage={data?.last}
            />

            <div className="flex flex-col justify-center">
              <div className="flex flex-col">
                <div className="p-3 text-2xl items-center">
                  {t("EndContest.endContestTitle")}
                </div>
                <div className="pl-7 text-lg">
                  {t("EndContest.participants")}
                </div>
              </div>
              <div className="pl-10 pb-5 pt-2 rounded w-[30vw]">
                {displayParticipants}
              </div>
            </div>
            <ChangePage
              pagination={paginationSettings}
              setPagination={setPaginationSettings}
              availablePageNumber={data?.totalPages}
            />
          </div>

          <Button
            extraStyle="mt-5 w-full xl:w-fit bg-red-400 hover:bg-red-200"
            onClick={() => handleEndContest()}
          >
            {t("EndContest.endContestButton")}
          </Button>
        </div>
      )}
    </>
  );
}

export default EndContest;
