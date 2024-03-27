import { useQuery } from "@tanstack/react-query";
import { Link } from "react-router-dom";
import getLatestContestThumbnails from "../services/contests/getLatestContestThumbnails";
import { useEffect, useState } from "react";
import logo from "../assets/logo-larger.png";
import { useTranslation } from "react-i18next";

function ImageContainer({ imageUrl }) {
  return (
    <div className="text backdrop">
      {imageUrl == "LOGO" ? (
        <div className="bg-slate-600 xl:h-60 xl:w-60 flex justify-center items-center">
          <img className="opacity-50" src={logo} />
        </div>
      ) : (
        <img
          className="col-span-1 row-span-1 xl:h-60 xl:w-60"
          src={imageUrl == "LOGO" ? logo : imageUrl}
        />
      )}
    </div>
  );
}

function LandingPage() {
  const { data } = useQuery({
    queryKey: ["latestThumbnails"],
    queryFn: getLatestContestThumbnails,
  });
  const [images, setImages] = useState([]);
  const { t } = useTranslation();

  useEffect(
    function () {
      if (!data) return;
      let imageArray = [];
      if (data.length >= 9) {
        imageArray = data.slice(0, 9);
      } else {
        const numExtraPhotosNeeded = 9 - data.length;
        const blanks = [];
        for (let i = 0; i < numExtraPhotosNeeded; i++) {
          blanks.push("LOGO");
        }
        imageArray = [...data, ...blanks];
      }
      const shuffled = imageArray.sort((a, b) => 0.5 - Math.random());
      setImages(shuffled);
    },
    [data]
  );

  return (
    <div className="w-full min-h-[83vh] bg-slate-800 xl:relative flex justify-left pl-12 xl:pl-56 items-center">
      <div className="hidden xl:grid xl:grid-cols-3 xl:grid-rows-3 xl:gap-4 border-r-2 pr-12 mr-12">
        {images.map((image, i) => (
          <ImageContainer key={"image" + i} imageUrl={image} />
        ))}
      </div>
      <div className="flex flex-col space-y-12">
        <h1 className="xl:text-5xl text-2xl w-8 xl:w-12 leading-normal font-bold text-white">
          {t("LandingPage.mainTitle")}
        </h1>
        <Link
          className="text-3xl text-white hover:translate-x-5 transition"
          to="/contests"
        >
          {t("LandingPage.contestsTitle")} {"➡️"}
        </Link>
      </div>
    </div>
  );
}

export default LandingPage;
