/**
 * @Copyright:   Gabriel Coman 2017
 * @Author:      Gabriel Coman (gabriel.coman@superawesome.tv)
 */
package com.gabrielcoman.simplexmediaplayer.aux.image;

import android.graphics.Bitmap;
import android.util.Base64;

import static android.graphics.BitmapFactory.decodeByteArray;

/**
 * Aux class that contains methods to transform base64 encoded strings to bitmaps to the video
 * player does not have any resource dependencies
 */
public class SimplexBitmap {

    /**
     * Method that generates the bottom video gradient bitmap
     *
     * @return a bitmap of a semi-translucent gradient (that will cover the bottom part of the
     *         video surface)
     */
    public static Bitmap createVideoGradientBitmap () {

        String data = "iVBORw0KGgoAAAANSUhEUgAAAAoAAAAICAYAAADA+m62AAAAAXNSR0IArs4c6QAA" +
                "ABxpRE9UAAAAAgAAAAAAAAAEAAAAKAAAAAQAAAAEAAAAYzTSV/QAAAAvSURBVCgV" +
                "YmBgYBAhEjOYAhXiwmZIcgyhQE4YEINodIwszlAGVADDpUhsmBiYBgAAAP//1nMT" +
                "5wAAAChJREFUY2BgYJiKhKcB2SCMLgbiM2wF4m1EYIarQEXXiMAM34CKCGIAAN0p" +
                "shJZ248AAAAASUVORK5CYII=";

        return createBitmapFromString(data);
    }

    /**
     * Method that generates the default play button bitmap
     *
     * @return a bitmap of a triangle play button
     */
    public static Bitmap createPlayButtonBitmap () {

        String data = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAA\n" +
                "ABxpRE9UAAAAAgAAAAAAAABAAAAAKAAAAEAAAABAAAAC083GaEMAAAKfSURBVHgB\n" +
                "7N3BShtRFMZxrW3VtgurG0UFV8FCKaRdpAHBJwgo+nA+hKuuqrs8QZJdyMvod8Yc\n" +
                "yqSJzkwmk7l3/hcON3cyEfKdX85GiRsbLBIgARIgARIgARIgARIgARIgARIgARIg\n" +
                "ARIgARIgARIgARIgARKYk8Cmrr1TbU3LHts1VgMSsEZb4z+qPqk+q3anZ7sOBIUQ\n" +
                "87JP+7ZqT3WkOp3u+9oNg8EAgkKIdVlzv6hOJpPJXbvdvtTjX6pzu6YCgkKIddl4\n" +
                "f6+yT3/rabpGo9HfTqdzo2u/Vd9UNhWAoBBiWwbgg8qa+90B+D4YDB4F4VbPAUEh\n" +
                "xLgcwIHe3A9v/OwOhBhb//KeMgFwEECID0IuAEAAgBtIdiZC+CAKTYCUAh2AEC6E\n" +
                "UgA4CCCEB6FUAEAAgBtIdiZC/UGsZAKkFOgAhPpCqASAgwBC/SBUCgAIAHADyc5E\n" +
                "WD+ItUyAlAIdgLA+CLUA4CCAUD2EWgEAAgDcQLIzEVYPopYTIKVAByCsDkIQABwE\n" +
                "EMqHEBQAIADADSQ7E2F5EEFOgJQCHYBQHEIUABxEv9+/b7VaV4qDv2LOaCIqAA6B\n" +
                "iZCx+7otSgBAAIAbSHYmwmIQUU+AlAIdgPA/hEYBcBBA+AehkQCAAAA3kOxNngiN\n" +
                "ngApBTo0EQIAZhU0DAIA5gDwS02YCADwbr+yxwwBAK80fvapGCEAYLbLGc4xQQBA\n" +
                "hoYvuiUGCABY1N0c10OGAIAcjX7r1hAhAOCtrhZ4PiQIACjQ4KwvCQECALJ2c4n7\n" +
                "6gwBAEs0Nu9LM0Co/Gv6AZC3iyXcPwfCsX5Ba9/XvKOq9JvZAVBCQ4v+iOFw+NDt\n" +
                "dq/V9J+qM9VXlX09v02CShYAinavxNeNx+M/vV7vQh0/VNk/6ygE4BkAAP//+Q6Q\n" +
                "RAAAArpJREFU7Z3LattAGIXTa3pb9LJpSQtdFVpsL1owXbibPEBJwH29PEJpF112\n" +
                "aXvtrcHP0p4/YbBRIiJZc5PmGziMJFszzvnOaAyZTI6O2pc7uuWB9Eqa/KMkcWCz\n" +
                "2fyZz+ffxOC19Fi6K0UpBCAJ8qtOl8vlr8lk8l2kP0vvpRfSQ4kAJOQSvOvFYvF7\n" +
                "NBqdC/RX6aN0Ij2XHkn3JBuYUQpPgOC4dx1UwH8S4XfSS+mp5EZ+NPjq8zJpfAfY\n" +
                "MQpy1AB81FFv4F3hCRAE+VWjOYMnAIWDJwABAtCHEe/Au5opwEMQ+gieABQOngB0\n" +
                "CECfR7wD72qmgBZBGBJ4AlA4eALQIABDHPEOvKuZAm4IQgngCUDh4AnAXgBKGvEO\n" +
                "vKuLngJKBl90AADv8Bf262DA78C7oyKmAMA73NfrQQcA8NeBV68MMgCAr2KuPx9U\n" +
                "AABfD7rulUEEYL1e/53NZj/0Q9oq25sWWyZbc1dnfC7Xex0ARnz3GPUyAIDvDt61\n" +
                "0KsAAN5h81f3IgCA9we82lLWAQB8FZf/8ywDAHj/oOtazCoAgK/DFO56FgEAfDjA\n" +
                "t7WcNACAvw1P+NeTBADw4cE27SFqAADfFEu890UJAODjAW3bU9AAAL4tjvjvDxIA\n" +
                "wMcHeWiPXgMA+EMxpLvPSwAAnw5g1547BQDwXe1Pf/9BAQB8enC+PkGrAADel+35\n" +
                "tNMoAIDPB5jvT+ICYLtVjvb+XvLyEPC+7c6vPQvAfcn2qf3gArBarX6Ox+MzXWOV\n" +
                "rUwYerEl08+kt9vt9mI6nZ7q+ItkmxhX97JlebVMGVqxrcmPJXsKvJEMutX7mxgD\n" +
                "XoYMtdg0YIBtt+onku1cbf+0wM4BLxNKKBYCexIYcJMd2zUKDuAADuAADuAADuAA\n" +
                "DuAADuAADuAADuAADuAADuAADuAADuAADuBADg78B7GrFQD03lnWAAAAAElFTkSu\n" +
                "QmCC";

        return createBitmapFromString(data);
    }

    /**
     * Method that generates the default replay button bitmap
     *
     * @return a bitmap of a curved arrow used as default replay button
     */
    public static Bitmap createReplayButtonBitmap () {

        String data = "iVBORw0KGgoAAAANSUhEUgAAAIAAAACACAYAAADDPmHLAAAAAXNSR0IArs4c6QAA\n" +
                "ABxpRE9UAAAAAgAAAAAAAABAAAAAKAAAAEAAAABAAAAI+eySSR8AAAjFSURBVHgB\n" +
                "7NzZkhRFFAZgF1xw3xdQHJZhGWFYDRHQCCL0xguDW5/Fe1+HR+AZfA1eQv8PJ4mi\n" +
                "6aWqurN6mcqIE3R3VZ48639OZtXwyivjGC0wWmC0wGiB0QI7b4FXo2EX2nmD7KqC\n" +
                "k05+LYqeCL3Rkcwxd5JffhrHJlmgOGjS0W9GyLdC74Q+CH0c+rQludccc/HAqwTQ\n" +
                "ZGDk0jiGtsCk06c5+rMI9WXo29CF0EHoWuhwAbnHveaYiwdegmcyMARFjYCgn4B+\n" +
                "/YgKEuXr8R2znP5hTPJ5aNLR1/Pb7dD9f3sOc4943Mi/0wLjk/zeRIpmMORSr0FP\n" +
                "fN4OQSAEgQSDa8dulGxglJLpTadfzO+cfa+nnztPy1oC41boamg/JPgEIbkmHdbF\n" +
                "acX574YP1DkV+ir0UYjukODYDMYQ9aKfUdfq9HlREtnuhQShYDgdggycyGldUIG+\n" +
                "dFV2Lpc181mAvR/Cq0tA5fbtG03HMyJjMirjDprpxQFd/o2MP4YOQmdCBRVO5nMJ\n" +
                "hHycOmS3gNFrXGqume/fhyCC6zsbANMc/00UvhL6sWmQbfgcmZUJAXshBMb1Chw4\n" +
                "rZbTXUPpnr3QC+Us3++G2AI67GQZoBQDUFDGF8ff3QZnz5Px8ePHf0cfTeReSBZD\n" +
                "tWZTVwKf7l+Hbk7yO3HixK/5XSAJEEiyM6MoLzMoxwCXQ1uX8ZNOm/wenSCC0qCe\n" +
                "C3IOF/QcquMH/Vcm55XvuWaubag57Lb1o5n1lN8LvRT9xQC78m901CMIch2+7v69\n" +
                "kIA4F5q5Zc21wxA7bX0ATMt6df6FurcrDp+lR/S9FQLr34VsZeeWu1xXRpQQ5WNr\n" +
                "EYDgIA/8HZusnxMED2MHpeHhrHvK77nnTkgzqVxuZSNYIN9+ttT6Y5X1xZl9/o3N\n" +
                "BIr+QfJsXQAQGHRp9Chxo48Rjvuc2G3/yIZbtRNoOt/hyO3j7si++sd2DoS2aicw\n" +
                "Or+vt6fMi/MdLG1NIzg6f4oTl/kpztcIek6w8Y3g6PxlPD1jbhy/FY3g6PwZDlz2\n" +
                "5xwJ/5Yg2OhGcHT+sl5eMD8BsLFHwqPzFzhvFZcTABt5JOyEzxm1ff641VuFp2fw\n" +
                "iH037ki4HO864XPIs3H7/MikedJBM56t1CJyn/tnPpyZ4Z/qPx/JtVFHwuU1Jse7\n" +
                "G3HCFzkehG6Hmu/s2T7ZQ3sGsYjc534BrenCB78H1T28YIHIsFE7AXXfnpRBn7/D\n" +
                "tkCHKpefPn36T2TwqFWT5AkbB3rMqiw5Pyen42ilahG5z/3mmY8Pfvjiv9b3FbK+\n" +
                "oCTXWo+Em3V/L8Ks5cFO1pURIPti6HSIszxj50COZiSBSt6uZJ75+OBXnt1bx3o3\n" +
                "Q79Uieg5TLOmIFz7kXAT+gd/kSMG4Hhn45rOL0IfhmSs7CVbcXo+Lj1K4OCLv3Ws\n" +
                "BxXOh+7M8dfKL2W9wxDUFZhkG3w0oX/ma0wr1/yIYbT1MsXZkFpdsr2Z6fm52mDw\n" +
                "ggxe5foopDT8EPqjls5NvlkH4tFdMA4eABYUeWrQXmgw6M9ast7bQ16nsj5YlpVk\n" +
                "GtoQ1hN0XvLUlcvK35uOqvU569w5WpP+gnHQweAgcOobrBWVlmHnQqDP+oJwcOWz\n" +
                "ZhnWloEQ4Gzop1q6T/LNWhJhLS+HiHpKa7QG6/qzli2YWs/YzazP17WM4nx9ALkG\n" +
                "7QEERNbcD0FBKDTYkP0gz3v71bdCjx49+jPrqHeinbKCb51ZLwHYQBAW56/l4Cvr\n" +
                "D/5ySDP7qzd+UfDnkHqv1DhlXDfkF+dr/CDgXuiHSXge6nvWvh4atBFsZv/cV5iX\n" +
                "NUIU43wQR0H1HsxxwDpHgX3773Oh6gg4z45ZXyNoGzpIIzh09st8zj8ZEnjrdn5E\n" +
                "eFZ6BOOpJ0+e/DXPOUNcixz+InmwRlD0U94JWNXID381H+xbb1OcH1GeyfLMBkM4\n" +
                "uM0akel8SHlkp2pD9oFgTc++M/c2wvW5J/xt9UQ1pTYB9iPG8yEJ1H/I9MKfb/fR\n" +
                "dRVzIocAKDuBaiiJsTrjb92vr0LwaTzC2972bEigrbvbjwgvjZIIdkGOnsmqVF0L\n" +
                "OQTSlLUlKKeG3/ea1zR7tPkt8+3GnIRWRYAC/zKz2qlfeDOmQ55Bmpqs02eUIIAE\n" +
                "UEpDSOauBEU0cGyq2bWlaxNE7hFwl0LmWr+qvYrCsvJim6jsc094O9t3vFvqfj5u\n" +
                "7GATSSHrbE37EITjOPqC8C6BZAvqQKw8AyEHmaoMjAlaDf7Du0A/QzBmNWXCe9WD\n" +
                "rMuQQNLrdA0iczje/Kr2qg7/UeAgBBKrQln4b/LoE0SD6CPSZOZ+H2hfNCd8/d83\n" +
                "apnGqiqUhf84OlpAVIImNefqImf2uR6+sl8DpSZWhbLwP06jIMpSOmMClnWrt/o4\n" +
                "eN6c8FT7PUkr2Z+P41jCAvxVegrIvXSf0Kz/K389OgLaC9tPC7Ix+2OEHqNkuvIJ\n" +
                "Rcuuwi5h6Z1Cqf8X5mVy32sR8GLI9tI6YwDECC1HcXrJdgkERZVqR/XnQ5dD7Nv7\n" +
                "rMAi6r/96UFfJ8+aF54j/McIHQefNJ0u2yWQLbpS6iDtpec0+U0QdD4tLAGgQTuc\n" +
                "5ci+v4fn7VB5lGmtcUy3ANugJsQXp3OsLL8emlmic+1SSDnohLQWVVPsz1f+1z7h\n" +
                "eTUEsrbt4CciDzLYv5ntTYjfzzVOb3Usn/scHUvkTrYmQNkBrPR9t6PXvCjhfKFT\n" +
                "VOb+XR/F8ZzVCuIXoXD4HIY6B0C1HUCEUf/L4Y91xvG/BTgf1HvQBLI9GynZPhPi\n" +
                "WwTAjfDRJ0B0a7Qa1XYAWd1j0I3669ZWFql/k2SAukrjudBKkBefUCd7ixIQVGsH\n" +
                "0CsiI8+uD9mvWz+zKKu7XA+/grhKSivELQGgblzrslibe8NTA6O5FGStISn37vJg\n" +
                "B6gL+lf69xZTeq6FdmwGQI0toADo3JQslHq7byg2B/81ku4gfCF6q6QrwnDSGAAx\n" +
                "wgCjts07bQVrCzMiwMsRVdvmnbaCtYUZA2CDAuA/AAAA//8Q0lQCAAAHkklEQVTt\n" +
                "nNtuHEUQhgM4gII45cApgNYRazlOYgc7UaJE4grufcsTccOD+Mrvwmv4JcL/rbus\n" +
                "9ux6d7Zd3XPqkkozcWaqu6r+OnT32HfuLNNH+tFd8QPx4QdnksyjIJsxGKvSpR1y\n" +
                "2vyFjIw/W9m8AqA8JHPb/EAqfVsBUN6xbUeMAXDknHQ/aBJz8VfinTYTiifzIsNk\n" +
                "aglY9gI2/1T8UPzS0+aS9178i/gL8cfijWQAIGUceE4GWZIJAFC0VT3Sc1MgbP6Z\n" +
                "+HvxK0+bI0/8gxj5rQCg5xapgpQx95xMAMBLyX0kBvEoXunSMfdkCCL1vafNJS/J\n" +
                "3iAl14SSEKn5jJmozYuAOz8//8cZAEkZN2dKoib9KqYmfSKeOlnJvS9DPPd0fsi4\n" +
                "h5LbeglozmBSWZqSMKk9yf9aDPKnXgbiYDvOAICt9gDkjwUZKnNtBlldojGZOgAo\n" +
                "t2RD9/ofgm2rPYCF94NT6NJzrQTeBYXpM6ZcBgA/WZBsuHd6evq3ZwZAnuRutQeg\n" +
                "56/oqjHxnJTJ0ii1DFxmP7Igq6IcG0C2B0CgtV4C6tkFZVsJhNREGfhOPOUyENv4\n" +
                "nQWH11W2PRGzt5Bk49zNydRXA9fSv5fTYzly/HMxq4ukTTcmyItZlidMVLJpUGg0\n" +
                "p7gpRPR/LiZCf48d53F/cXHxn+TG9R9/bk1XfcDOzs5fHhOLZWg2b8W2Tz2lZtCC\n" +
                "i+bvifiP2C4e95IZN9qALYmy1igU1ayeiskyU8oCgJ3G7Eex+9o/2JUdQJrLpPqv\n" +
                "9xYEUrN1qWGizSyQlKrCfIdwQT/ADuj3vbd+sSkk2S6rLCZr69S59zr1cqpXvQAn\n" +
                "hIAtOV3p3SEQ0c/Gz8/iN2YDzytyg/yk5Z/evUZWBh7rp7kmzIpgV0xNJDrGCgL0\n" +
                "AuSA3f2o3UCEbLFbWY1T1lMbxPuqCb8W0xB+KSbrMO6YCOcDbkAO2F2Pfc0fyBW7\n" +
                "H7ZZ2sJBb20w76tkszlEY0TqGtOqwJzPkS/Oee1tO5Mn2Vk22IpkAZSQAqwK2BsY\n" +
                "Sz/QdP6JOSvHVXZzaf4kZ4lKZQHrB77RDIbeD5R2Ps0fvZpL89dEQMksQD9Aqhxy\n" +
                "U1jU+VH2dGv+mgDg30WyQFCGT8cAAZmAcsDYQ2kMl5x/dnb2b46UbzJlm+Z+in7k\n" +
                "T8WyQAABmWBXTE9AWhvC6mDJ+eaknFfZZl+cNfolf0GWBbJtZMSG0oj0BDSGrA5Y\n" +
                "Iva1LyA4sA3ZitJF9sra8JmdNM6x+CdxkdVTnAX2bRK5r1KO5Q3LUIzbt5JA1HNy\n" +
                "igPIVmStbEu92NYahwCZiVliJh376r2tCaSjbLbDjFhJu9d4VhLYSWM7lWzQZW8Q\n" +
                "Rz0OwB5kqyybPGaH+BrGK75sRnHQhtIzcUmFQfyBmGxAzWsCgbnlJOTDAA8AMj6A\n" +
                "nImznOzFDo/vGU9cLPVrrGtE2iMVg75sW8SxwvG9xqTrJdoAAnPAEcyHRtGyghcY\n" +
                "zOnojHzGYTwAyPgAslgQYAfGE8/ERVO/xrtGcSlw/aXG2Nnr7jUbgIADaLr4xpAe\n" +
                "wcBAlsJhOM6cuM3VHI4cczryH4kZj3GzbY1v0LsXO6YYEwPTnRMJRRqfVYbR2EQE\n" +
                "jeKemLnwqRURSoTcE+NAUjbO3MQ8x/O8x/vIQR5ykX8kLhrxsc4aGz17c2YCCDAY\n" +
                "kbEr7swwZiTmIKY+8lHkXGyAoFZTLjYxz5nDeR85yOuDbjTC6NOrU9NO+wFz/Kpr\n" +
                "+OUIAHEiJnKI3sMNzHM837nDY52Yj5ggI9gIOuzeG+q8H4iNNbZ7Ph2Tp3tR929C\n" +
                "XLMfKLILNjZH36SPjN6rur8OBDRYNE7FtkJvMtpYfi5bUo56V/c1p5VEXaI+VRA4\n" +
                "IFB2fCUmmLAnwUWm7T1VEPg4n8jH+b1s+jahsIIgEQT8FpaMS80n7RP5vev4NadW\n" +
                "FIMAZTrZLUz0QyevyUYs9ej27fibtI8dB0tMHiXYuCh+WtaJFxMHlX3stPOB7tmF\n" +
                "ZJd1EDVf81xLtkREKZTbFXe2bZzon6yvyR6W8qn3bEPbgZZux0GAAKVQDiVrSRCk\n" +
                "ZIdmyh9svZcurcj6gsmXBFmL84WZeHQpXzqtpWZJmOnp41y/KZs1dycIl64W9XzM\n" +
                "QZc/ypQvvdZSXBIwAg0iX7Z2cr6e4MekV6RfM+oH3+VLp1uRrRJoEO+L+dqYZdCo\n" +
                "gIA+YgA+6aiX/ivJsgENUPyp1aCBEP5OD7+uhR40vQAcoE8+6mWDlbQOCG9y/YGK\n" +
                "pFy+5iVpRo1nWbcnfizG8QAbgI9ueSed3GkVEDDkXIxh3f+m3hp/tvovbd/+qXkR\n" +
                "7Qdi9u/t+0QivjpeRkihGAgYkv0DPsgknRJdCzB0lRk0PpHOgQ2fjP0mjqOdzp6d\n" +
                "PHoc9Kh0CwtgQAyJQTFsEwxkhmfiIzHHp1k+5UKu2BzOmACRbwdJ8axkmtFeHS+j\n" +
                "eBIGXQUGjM8ftm5+zAkoDsUAI5V5Hzk3ORxA3hXXaJcRSlITDDiBWmsZwkDBLttt\n" +
                "GXAhjwhf5fAa6TJM12SAiDMEoPDiOMKrw7v2dsvxY1Dc9r7lkPWxaoFqgWqBaoFq\n" +
                "gWqBaoFqgWqBnlvgfz85WiOn00+LAAAAAElFTkSuQmCC";

        return createBitmapFromString(data);
    }

    /**
     * Generic method that decodes a base64 string and returns a bitmap
     *
     * @param data  the base64 encoded string
     * @return      a bitmap
     */
    private static Bitmap createBitmapFromString (String data) {

        try {
            byte [] encodeByte= Base64.decode(data, Base64.DEFAULT);
            return decodeByteArray(encodeByte, 0, encodeByte.length);
        } catch(Exception e) {
            e.getMessage();
            return null;
        }

    }
}
