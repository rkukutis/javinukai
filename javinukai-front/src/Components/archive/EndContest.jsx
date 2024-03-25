import { useState } from "react";
import { useMutation, useQuery } from "@tanstack/react-query";
import { useTranslation } from "react-i18next";
import getCompetingUsers from "../../services/users/getCompetingUsers";
import Button from "../Button";
import endCompetition from "../../services/archive/endCompetition";
import toast from "react-hot-toast";
import ParticipantCard from "./ParticipantCard";
import SpinnerPage from "../../pages/SpinnerPage";
import { useNavigate } from "react-router-dom";

const defaultPagination = {
  page: 0,
  limit: 25,
  sortBy: "email",
  sortDesc: "false",
  searchedField: null,
};

function EndContest({ contest, close }) {
  const { id, name } = contest;
  console.log("contest in end contest -> ", contest);
  const [paginationSettings, setPaginationSettings] =
    useState(defaultPagination);
  const [winners, setWinners] = useState([]);
  const navigate = useNavigate();

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
    if (!window.confirm(t("EndContest.confirmationQuestion"))) return;
    mutate({ id, winners });
    close();
  };

  const { mutate } = useMutation({
    mutationFn: (data) => endCompetition(data),
    onSuccess: () => {
      navigate("/contests");
      toast.success(t("EndContest.endContestSucess"));
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
            <div className="flex flex-col justify-center">
              <div className="flex flex-col">
                <div className="mt-10 mb-5 text-2xl text-center items-center">
                  <p className="text-3xl">{t("EndContest.endContestTitle")} </p>
                  <p className="text-pink-600 w-30 inline-block">{name}</p>
                </div>
                <div className="pl-7 text-lg">
                  {t("EndContest.participants")}
                </div>
              </div>
              <div className="pl-10 pb-5 pt-2 rounded w-full">
                {displayParticipants}
              </div>
            </div>
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
