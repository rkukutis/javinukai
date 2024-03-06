import { useParams } from "react-router-dom";
import getContest from "../services/contests/getContest";
import { useQuery } from "@tanstack/react-query";
import contestPhoto from "../assets/contest-photo.jpg";
import formatTimestap from "../utils/formatTimestap";

import { useState } from "react";
import getCategories from "../services/categories/getCategories";
import { CategoryItem } from "../Components/category/CategoryItem";
import { useTranslation } from "react-i18next";

function ContestDetailsPage() {
  const { contestId } = useParams();
  const { t } = useTranslation();
  const [expandedCategory, setExpandedCategory] = useState("");
  const { data: contest, isFetching: isFetchingContest } = useQuery({
    queryKey: ["contest", contestId],
    queryFn: () => getContest(contestId),
  });
  const { data: categories, isFetching: isFetchingCategories } = useQuery({
    queryKey: ["contestCategories", contestId],
    queryFn: () => getCategories(contestId),
  });

  return (
    <div className="w-full min-h-[82vh] flex flex-col items-center bg-slate-50">
      <div className="w-full xl:w-3/4 pb-4 px-6 bg-white shadow-md">
        <div className="flex flex-col space-y-4">
          <section className="relative">
            <img
              className="w-full h-[30rem] object-cover"
              src={contestPhoto}
              alt="contest-thumbnail"
            />
            <div className="absolute top-0 left-0 w-full h-full backdrop-blur-sm backdrop-brightness-90 pr-6 back">
              <div className="ml-12 mt-12 flex-col space-y-6">
                <h2 className="text text-lg top-4 text text-white xl:text-3xl">
                  {t("ContestDetailsPage.contest")}
                </h2>
                <h1 className="text text-xl xl:text-5xl text-white font-semibold">
                  {contest?.name}
                </h1>
                <div className="flex flex-col lg:flex-row lg:space-x-5 text-white text-xl">
                  <span>
                    {t("ContestDetailsPage.startDate")}:{" "}
                    {formatTimestap(contest?.startDate)}
                  </span>
                  <span>
                    {t("ContestDetailsPage.endDate")}:{" "}
                    {formatTimestap(contest?.endDate)}
                  </span>
                </div>
              </div>
            </div>
          </section>
          <section className="text text-slate-700 leading-loose text-lg">
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              {t("ContestDetailsPage.description")}
            </h1>
            <p>{contest?.description}</p>
          </section>
          <section>
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              {t("ContestDetailsPage.categories")}
            </h1>
            <div className="flex-col space-y-1 mt-4">
              {categories?.map((category) => (
                <CategoryItem
                  key={category.id}
                  categoryInfo={category}
                  contestInfo={contest}
                  expandedCategory={expandedCategory}
                  onSetExpandedCategory={setExpandedCategory}
                />
              ))}
            </div>
          </section>
        </div>
      </div>
    </div>
  );
}

export default ContestDetailsPage;
