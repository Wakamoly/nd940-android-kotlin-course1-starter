package com.udacity.shoestore.repositories

import com.udacity.shoestore.Resource
import com.udacity.shoestore.UserPreferences
import com.udacity.shoestore.base.BaseRepository
import com.udacity.shoestore.db.dao.ShoeDao
import com.udacity.shoestore.db.dao.UserDao
import com.udacity.shoestore.db.entities.ShoeEntity
import com.udacity.shoestore.db.entities.UserEntity

class RegisterRepository (
        private val userPreferences: UserPreferences,
        private val userDao: UserDao,
        private val shoeDao: ShoeDao
) : BaseRepository() {

    suspend fun login(
            email: String, password: String, username: String
    ) : Resource<UserEntity>{
        val userEntity = UserEntity(username, email, "token")
        if (safeApiCall { userDao.insert(userEntity) } is Resource.Success){
            saveCredentials(username, email)
            val shoeList: MutableList<ShoeEntity> = ArrayList()
            shoeList.add(ShoeEntity("1930's FRENCH NOS Art Deco Wingtips",7.5,"Unknown","These are 100% one of the most wonderful pairs of shoes I have ever seen! Late 1930's French Custom-Made wingtips in the 1920's Art Deco style. These are the only pair I have seen that even remotely resemble this incredibly fabulous style. They are just a bit too small for my feet... and I have been regretting this fact for years now. Time to send them off to a VERY lucky size 7 1/2D US.", listOf("https://www.vintageshoesaddict.com/art/shoes/frenchartdeco1.jpg", "https://www.vintageshoesaddict.com/art/shoes/frenchartdeco2.jpg")))
            shoeList.add(ShoeEntity("1940's NOS Unworn Woven/ Braided Oxfords",7.5,"El Chico","Woven shoes were one of the absolutely coolest styles of the 1940's. Extremely wonderful interwoven leather design, a fabulous casual shoe.", listOf("https://www.vintageshoesaddict.com/art/shoes/elchicowoven1.jpg","https://www.vintageshoesaddict.com/art/shoes/elchicowoven2.jpg")))
            shoeList.add(ShoeEntity("1940's NOS Unworn Hanover ELEGANT Black Wingtip Derby",7.5,"Hanover","Just look at that beautiful shape! So very Vintage in style, yet they translate easily to today. Paired with a classic suit, these really set the look OFF!", listOf("https://www.vintageshoesaddict.com/art/shoes/hanovernoswingtips1.jpg","https://www.vintageshoesaddict.com/art/shoes/hanovernoswingtips2.jpg","https://www.vintageshoesaddict.com/art/shoes/hanovernoswingtips3.jpg")))
            shoeList.add(ShoeEntity("1940's NOS (unworn) Norwegian Spectators",8.0,"Unknown","Unworn and mint contition, these are some really beautiful Spectators. These are made in the UK and are a great example of Vintage top-line shoemaking & styling.", listOf("https://www.vintageshoesaddict.com/art/shoes/norwegianspectators1.jpg","https://www.vintageshoesaddict.com/art/shoes/norwegianspectators2.jpg")))
            shoeList.add(ShoeEntity("1940's Florsheim Buffalo Skin Perforated Spectators",8.0,"Florsheim","These are really wonderful shoes. A pair of 1940's Florsheim PERFORATED SPECTATORS (punch holes throughout the shoe allow a cool breeze to flow in and cool your foot. The fact that they are made with Vintage Buffalo Skin only adds to the Cool-Factor.", listOf("https://www.vintageshoesaddict.com/art/shoes/florsheimperforatedaprons1.jpg","https://www.vintageshoesaddict.com/art/shoes/florsheimperforatedaprons2.jpg")))
            shoeList.add(ShoeEntity("Antelope Skin Wholecut Oxfords",8.0,"Dack's","The antelope skin on these is just perfect. Very durable and will last 2 lifetimes. The fact that this pair is a wholecut (made from a single piece of skin) is excellent to show off the beauty of Exotic Skin Vintage Shoes.", listOf("https://www.vintageshoesaddict.com/art/shoes/dacksantelope1.jpg","https://www.vintageshoesaddict.com/art/shoes/dacksantelope2.jpg")))
            shoeList.add(ShoeEntity("1940's Bostonian for Boyd's ULTIMATE GUNBOAT Captoes",8.5,"Boyd's","Straight from my personal collection, I offer these... the KING of all Gunboats. I do have a second pair that I am keeping... check out the gunboat page where aI describe the model in more detail. These babies weigh well over 4 pounds and are some Heavy Duty shoes!", listOf("https://www.vintageshoesaddict.com/art/shoes/boydscaptoegunboat1.jpg","https://www.vintageshoesaddict.com/art/shoes/boydscaptoegunboat2.jpg")))
            shoeList.add(ShoeEntity("1950's Florsheim Gunboat Spectators Worn 1 time",8.5,"Florsheim","Another pair straight from my personal collection, I offer these... a TRUE Longwing Gunboat SPECTATOR Shoe. This shoe is easily found in either straight black or brown. HOWEVER, this SPECTATOR 2-Tone model is extremely rare and pretty darned awesome. I wore them only 1 time after purchasing them new NOS. Note the tan colored welt stitch... a very nice detail.", listOf("https://www.vintageshoesaddict.com/art/shoes/florsheimgunboatspecs1.jpg","https://www.vintageshoesaddict.com/art/shoes/florsheimgunboatspecs2.jpg")))
            shoeList.add(ShoeEntity("1950's NOS Unworn Florsheim BUFFALO SKIN Derby Wingtips",9.0,"Florsheim","Buffalo skin is one of the most durable skins ever used to make shoes. It can get wet and dries perfectly. Drying out is basically a non-issue. The added bonus is the patina that builds over time with this elather. Really incredible.", listOf("https://www.vintageshoesaddict.com/art/shoes/florsheimbuffalonos1.jpg","https://www.vintageshoesaddict.com/art/shoes/florsheimbuffalonos2.jpg")))
            shoeList.add(ShoeEntity("1940's Near-NOS (worn 1x) Stetson brown Calf & white Suede Spectator Shoes",9.5,"Stetson","After you check out this site, you will know that I LOVE SPECS! Fantastic summer style and a ton of fun to wear. These are 1940's Once-Worn (near brand new) Stetsons. Condition is clearly excellent!", listOf("https://www.vintageshoesaddict.com/art/shoes/stetsonspectatorshoes1.jpg","https://www.vintageshoesaddict.com/art/shoes/stetsonspectatorshoes2.jpg")))
            shoeList.add(ShoeEntity("1950's Edwin Clapp Giant Sea Turtle Saddle Wingtips",10.0,"Edwin Clapp","THE true Holy Grail of Vintage Sea Turtle shoes. These are AMAZING. The rarest of the rare in Vintage Exotic Skins (Sea Turtle is endangered now and hasn't been legally sold for over a decade. The best of the best in Vintage shoemaking... Edwin Clapp. You already know the name. Not much more to say about these. You will not find another pair in a wearable size!!!", listOf("https://www.vintageshoesaddict.com/art/shoes/clappstwingtips1.jpg","https://www.vintageshoesaddict.com/art/shoes/clappstwingtips2.jpg","https://www.vintageshoesaddict.com/art/shoes/clappstwingtips3.jpg")))
            for (entity in shoeList){
                safeApiCall { shoeDao.insert(entity) }
            }
        }
        return safeApiCall { userDao.getUser(username) }
    }

    private suspend fun saveCredentials(username: String, email: String){
        userPreferences.saveCredentials(username, email)
    }

}