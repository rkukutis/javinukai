import { useState } from "react";
import { useForm } from "react-hook-form";
import axios from "axios";
import FormFieldError from "../FormFieldError";
import { useTranslation } from "react-i18next";
import toast from "react-hot-toast";
import StyledInput from "../StyledInput";
import { getContestCreationMultipart } from "../../utils/getMultipartForm";
import { CategorySelection } from "./CategorySelection";
import { useQueryClient } from "@tanstack/react-query";
import BackButton from "../BackButton";

export default function CreateContest({
  initialContestInfo,
  initialCategories,
  contestTitle,
  saveTitle,
}) {
  const queryClient = useQueryClient();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm({
    defaultValues: {
      ...initialContestInfo,
      ...(initialContestInfo && {
        startDate: initialContestInfo.startDate.split("T")[0],
      }),
      ...(initialContestInfo && {
        endDate: initialContestInfo.endDate.split("T")[0],
      }),
    },
  });

  const [selectedCategories, setSelectedCategories] = useState(
    initialCategories ?? []
  );
  const { t } = useTranslation();
  const [thumbnailFile, setThumbnailFile] = useState();

  const onSubmit = async (data) => {
    if (!window.confirm(t("ContestCard.editdeleteContestConfirm"))) return;
    console.log(data);
    const startDate = new Date(data.startDate).toISOString();
    const endDate = new Date(data.endDate).toISOString();

    if (isNaN(Date.parse(startDate)) || isNaN(Date.parse(endDate))) {
      toast.error(t("CreateContest.invalidDatesError"));
      return;
    }
    const contestData = {
      ...data,
      startDate,
      endDate,
      categories: selectedCategories,
    };

    if (initialContestInfo) {
      await axios
        .put(
          `${import.meta.env.VITE_BACKEND}/api/v1/contests/${
            initialContestInfo.id
          }`,
          getContestCreationMultipart(contestData, thumbnailFile),
          {
            withCredentials: true,
          }
        )
        .then(() => {
          toast.success(t("CreateContest.contestDetailsUpdatedSuccess"));
          queryClient.invalidateQueries(["contest", "contestCategories"]);
        })
        .catch(() =>
          toast.error(t("CreateContest.contestDetailsUpdatedError"))
        );
      // update categories separately
      if (initialCategories != selectedCategories) {
        await axios
          .patch(
            `${import.meta.env.VITE_BACKEND}/api/v1/contests/${
              initialContestInfo.id
            }`,
            selectedCategories,
            {
              withCredentials: true,
            }
          )
          .then(() => {
            toast.success(t("CreateContest.contestCategoriesUpdatedSuccess"));
            queryClient.invalidateQueries(["contest", "contestCategories"]);
          })
          .catch(() =>
            toast.error(t("CreateContest.contestCategoriesUpdatedError"))
          );
      }
    } else {
      await axios
        .post(
          `${import.meta.env.VITE_BACKEND}/api/v1/contests`,
          getContestCreationMultipart(contestData, thumbnailFile),
          {
            withCredentials: true,
          }
        )
        .then(() => toast.success(t("CreateContest.contestCreatedSuccess")))
        .catch(() => toast.error(t("CreateContest.contestCreatedError")));
    }
  };

  function handleThumbnailFileAdd(e) {
    e.preventDefault();
    if (e.target.files.length != 1) {
      toast.error(t("CreateContest.thumbnailError"));
      return;
    }
    setThumbnailFile(e.target.files[0]);
  }

  return (
    <div
      className={`${
        initialContestInfo ? "w-full" : "w-3/5 my-5"
      } mx-auto p-6 bg-white rounded-md shadow-md`}
    >
      <h2 className="text-2xl font-semibold mb-4">
        {contestTitle ? contestTitle : t("CreateContest.contestTitle")}
      </h2>
      <form id="contest-create-form" onSubmit={handleSubmit(onSubmit)}>
        <div className="mb-4">
          <label
            htmlFor="name"
            className="block text-sm font-medium text-gray-700"
          >
            {t("CreateContest.contestName")}
          </label>
          <input
            type="text"
            id="name"
            {...register("name", { required: t("CreateContest.required") })}
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.name && (
            <FormFieldError>{errors.name.message}</FormFieldError>
          )}
        </div>
        <div className="mb-4">
          <label
            htmlFor="thumbnailFile"
            className="block text-sm font-medium text-gray-700"
          >
            {t("CreateContest.contesThumbnail")}
          </label>
          <input
            onChange={handleThumbnailFileAdd}
            type="file"
            id="thumbnailFile"
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
        </div>
        <div className="mb-4">
          <label
            htmlFor="description"
            className="block text-sm font-medium text-gray-700"
          >
            {t("CreateContest.description")}
          </label>
          <textarea
            id="description"
            {...register("description", {
              required: t("CreateContest.required"),
            })}
            className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
          />
          {errors.description && (
            <FormFieldError>{errors.description.message}</FormFieldError>
          )}
        </div>
        <section className="xl:grid xl:grid-cols-2 xl:grid-rows-2 xl:gap-3">
          <div>
            <label
              htmlFor="startDate"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.startDate")}
            </label>
            <input
              type="date"
              id="startDate"
              {...register("startDate", {
                required: t("CreateContest.required"),
                min: { value: 1 },
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.startDate && (
              <FormFieldError>{errors.startDate.message}</FormFieldError>
            )}
          </div>
          <div>
            <label
              htmlFor="endDate"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.endDate")}
            </label>
            <input
              type="date"
              id="endDate"
              {...register("endDate", {
                required: t("CreateContest.required"),
                min: 1,
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.endDate && (
              <FormFieldError>{errors.endDate.message}</FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label
              htmlFor="maxTotalSubmissions"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.maxTotalSubmissions")}
            </label>
            <input
              min={0}
              placeholder={500}
              type="number"
              id="maxTotalSubmissions"
              {...register("maxTotalSubmissions", {
                required: t("CreateContest.required"),
                min: 1,
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.maxTotalSubmissions && (
              <FormFieldError>
                {errors.maxTotalSubmissions.message}
              </FormFieldError>
            )}
          </div>
          <div className="mb-4">
            <label
              htmlFor="maxUserSubmissions"
              className="block text-sm font-medium text-gray-700"
            >
              {t("CreateContest.maxSubmissionsPerUser")}
            </label>
            <input
              min={0}
              placeholder={50}
              type="number"
              id="maxUserSubmissions"
              {...register("maxUserSubmissions", {
                required: t("CreateContest.required"),
                min: 1,
              })}
              className="mt-1 p-2 w-full border-2 border-slate-100 rounded-md focus:outline-none focus:border-blue-500"
            />
            {errors.maxUserSubmissions && (
              <FormFieldError>
                {errors.maxUserSubmissions.message}
              </FormFieldError>
            )}
          </div>
        </section>
      </form>
      <CategorySelection
        selectedCategories={selectedCategories}
        setSelectedCategories={setSelectedCategories}
      />
      <div className="flex justify-end">
        <BackButton />
        <StyledInput
          form="contest-create-form"
          extraStyle="px-2 text-lg font-bold"
          value={saveTitle ? saveTitle : t("CreateContest.creation")}
          type="submit"
        />
      </div>
    </div>
  );
}
