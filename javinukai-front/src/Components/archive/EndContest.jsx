import { useState } from "react";
import PaginationSettings from "../PaginationSettings";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import getCompetingUsers from "../../services/users/getCompetingUsers";
import Button from "../Button";
import endCompetition from "../../services/archive/endCompetition";
import toast from "react-hot-toast";
import ParticipantCard from "./ParticipantCard";

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
        <div>{t("EndContest.endContestTitle")}</div>
        <div>{t("EndContest.participants")}</div>
        <div>{displayParticipants}</div>

        <Button onClick={() => handleEndContest()}>
          {t("EndContest.endContestButton")}
        </Button>
      </div>
    </div>
  );
}

export default EndContest;
