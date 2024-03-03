import { Outlet, useNavigate, useParams } from "react-router-dom";
import getContest from "../services/contests/getContest";
import { useQuery } from "@tanstack/react-query";
import contestPhoto from "../assets/contest-photo.jpg";
import formatTimestap from "../utils/formatTimestap";
import expandMoreIcon from "../assets/icons/expand_more_FILL0_wght400_GRAD0_opsz24.svg";
import expandLessIcon from "../assets/icons/expand_less_FILL0_wght400_GRAD0_opsz24.svg";

import { useState } from "react";
import Button from "../Components/Button";
import getCategories from "../services/categories/getCategories";

function CategoryItem({
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
        <h1 className="text text-xl">{categoryInfo.name}</h1>
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
              onClick={() => navigate(`category/${categoryInfo.id}/entries`)}
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
function ContestDetailsPage() {
  const { contestId } = useParams();
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
                  Contest
                </h2>
                <h1 className="text text-xl xl:text-5xl text-white font-semibold">
                  {contest?.name}
                </h1>
                <div className="flex flex-col lg:flex-row lg:space-x-5 text-white text-xl">
                  <span>Start date: {formatTimestap(contest?.startDate)}</span>
                  <span>End date: {formatTimestap(contest?.endDate)}</span>
                </div>
              </div>
            </div>
          </section>
          <section className="text text-slate-700 leading-loose text-lg">
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              Description
            </h1>
            <p>{contest?.description}</p>
          </section>
          <section>
            <h1 className="text-2xl text-teal-500 font-bold py-2">
              Categories
            </h1>
            <div className="flex-col space-y-1 mt-4">
              {categories?.map((category) => (
                <CategoryItem
                  key={category.id}
                  categoryInfo={category}
                  contestId={contest?.id}
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
