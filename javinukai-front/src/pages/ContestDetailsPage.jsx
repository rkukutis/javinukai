import { useNavigate, useParams } from "react-router-dom";
import getContest from "../services/contests/getContest";
import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import contestPhoto from "../assets/contest-photo.jpg";
import formatTimestap from "../utils/formatTimestap";
import { useState } from "react";
import getCategories from "../services/categories/getCategories";
import { CategoryItem } from "../Components/category/CategoryItem";
import { useTranslation } from "react-i18next";
import Button from "../Components/Button";
import toast from "react-hot-toast";
import sendParticipationRequest from "../services/participation-requests/sendParticipationRequest";
import useUserStore from "../stores/userStore";
import { ParticipationStatus } from "../Components/contest/ParticipationStatus";
import Modal from "../Components/Modal";
import CreateContest from "../Components/Contest-Components/CreateContest";
import EndContest from "../Components/archive/EndContest";
import BackButton from "../Components/BackButton";
import deadlineReached from "../utils/deadlineReached";
import startNewContestStage from "../services/contests/startNewContestStage";
import deleteContest from "../services/contests/deleteContest";

function EditContestSection({ contestInfo, categoriesInfo }) {
  const { t } = useTranslation();
  const [modalOpen, setModalOpen] = useState(false);
  const [modalEndOpen, setModalEndOpen] = useState(false);
  const { user } = useUserStore((state) => state);
  const navigate = useNavigate();
  const queryClient = useQueryClient();

  return (
    <div className="flex space-x-2 justify-end">
      {(user.role === "ADMIN" || user.role === "MODERATOR") && (
        <Button
          onClick={() => {
            if (confirm(t("StartNewContestStage.newStageConfirm"))) {
              startNewContestStage(contestInfo.contest.id);
            }
          }}
        >
          {t("ContestCard.startNewStage")}
        </Button>
      )}

      {user.role === "ADMIN" && (
        <>
          <Button
            onClick={() => setModalOpen(true)}
            extraStyle="bg-orange-500 hover:bg-orange-300"
          >
            {t("ContestCard.editContest")}
          </Button>
          <Modal isOpen={modalOpen} setIsOpen={setModalOpen}>
            <CreateContest
              contestTitle={t("ContestDetailsPage.contestEditTitle")}
              saveTitle={t("ContestDetailsPage.contestEditSave")}
              initialContestInfo={contestInfo?.contest}
              initialCategories={categoriesInfo}
            />
          </Modal>
          <Button
            id="endContestButton"
            onClick={() => setModalEndOpen(true)}
            extraStyle="bg-orange-500 hover:bg-orange-300"
          >
            {t("ContestCard.endContest")}
          </Button>
          <Modal isOpen={modalEndOpen} setIsOpen={setModalEndOpen}>
            <EndContest
              contest={contestInfo?.contest}
              close={() => setModalEndOpen(false)}
            />
          </Modal>
          <Button
            extraStyle="bg-red-500 hover:bg-red-300"
            onClick={() => {
              if (confirm(t("DeleteContest.DeleteContestConfirm"))) {
                deleteContest(contestInfo.contest.id, queryClient);
                navigate("/contests");
              }
            }}
          >
            {t("ContestCard.deleteContest")}
          </Button>
        </>
      )}
    </div>
  );
}

function ContestDetailsPage() {
  const { user } = useUserStore((state) => state);
  const { contestId } = useParams();
  const { t } = useTranslation();
  const queryClient = useQueryClient();
  const [expandedCategory, setExpandedCategory] = useState("");
  const { data } = useQuery({
    queryKey: ["contest", contestId],
    queryFn: () => getContest(contestId),
  });
  const { data: categories } = useQuery({
    queryKey: ["contestCategories", contestId],
    queryFn: () => getCategories(contestId),
  });

  const { mutate: requestMutation } = useMutation({
    mutationFn: () => sendParticipationRequest({ contestId, t }),
    onSuccess: () => {
      toast.success(t("ContestDetailsPage.participationSuccess"));
      queryClient.invalidateQueries(["contest"]);
    },
    onError: () => {
      toast.error(t("ContestDetailsPage.participationError"));
    },
  });

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
      <div className={`w-full xl:w-3/4 pb-4 px-6 bg-white shadow-md`}>
        <div className="flex flex-col space-y-4">
          <section className="relative">
            <img
              className="w-full h-[30rem] object-cover"
              src={data?.contest.thumbnailURL ?? contestPhoto}
              alt="contest-thumbnail"
            />
            <div className="absolute top-0 left-0 w-full h-full backdrop-blur-sm backdrop-brightness-50 pr-6 back">
              <div className="ml-12 mt-12 flex-col space-y-6">
                <h2 className="text text-lg top-4 text text-white xl:text-3xl">
                  {t("ContestDetailsPage.contest")}
                </h2>
                <h1 className="text text-xl xl:text-5xl text-white font-semibold">
                  {data?.contest.name}
                </h1>
                <div className="flex flex-col lg:flex-row lg:space-x-5 text-white text-xl">
                  <span>
                    {t("ContestDetailsPage.startDate")}:{" "}
                    {formatTimestap(data?.contest.startDate)}
                  </span>
                  <span>
                    {t("ContestDetailsPage.endDate")}:{" "}
                    {formatTimestap(data?.contest.endDate)}
                  </span>
                </div>
                <div>
                  <ParticipationStatus status={data?.status} />
                </div>
                {data?.status == "ACCEPTED" && (
                  <h1 className="text-xl text-white">
                    {t("ContestDetailsPage.userEntries")}: {data?.userEntries} /{" "}
                    {data?.maxUserEntries}
                  </h1>
                )}

                <h2 className="text-white text-xl">
                  {t("ContestDetailsPage.entries")}: {data?.totalEntries} /{" "}
                  {data?.contest.maxTotalSubmissions}
                </h2>
              </div>
            </div>
          </section>

          <EditContestSection contestInfo={data} categoriesInfo={categories} />
          <section className="text text-slate-700 leading-loose text-lg">
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              {t("ContestDetailsPage.description")}
            </h1>
            <p>{data?.contest.description}</p>
          </section>
          <section>
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              {t("ContestDetailsPage.categories")}
            </h1>
            <div className="flex-col space-y-1 mt-4">
              {categories?.map((category) => (
                <CategoryItem
                  canParticipate={data?.status == "ACCEPTED"}
                  contestLimitReached={
                    data?.maxUserEntries == data?.userEntries ||
                    data?.contest.maxTotalSubmissions == data?.totalEntries
                  }
                  key={category.id}
                  categoryInfo={category}
                  contestInfo={data?.contest}
                  expandedCategory={expandedCategory}
                  onSetExpandedCategory={setExpandedCategory}
                />
              ))}
            </div>
          </section>
        </div>
        <div className="mt-4">
          {!data?.status &&
            user &&
            user.role !== "JURY" &&
            !deadlineReached(data?.contest.endDate) && (
              <Button onClick={requestMutation}>
                {t("ContestDetailsPage.participate")}
              </Button>
            )}
          <BackButton />
        </div>
      </div>
    </div>
  );
}

export default ContestDetailsPage;
