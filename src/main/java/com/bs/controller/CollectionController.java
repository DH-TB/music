package com.bs.controller;

import com.bs.entity.*;
import com.bs.entity.Collection;
import com.bs.repository.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Api(value = "我的歌单")
@RestController
@RequestMapping("/api")
public class CollectionController {
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MusicRepository musicRepository;
    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private MusicLeaderRepository musicLeaderRepository;
    @Autowired
    private SignerLeaderRepository signerLeaderRepository;
    @Autowired
    private SignerRepository signerRepository;
    @Autowired
    private MusicStyleRepository musicStyleRepository;
    @Autowired
    private UserStyleRepository userStyleRepository;

    //   获取我的收藏列表
    @GetMapping("/collection/{userId}")
    public ResponseEntity getAllCollectionMusic(@PathVariable("userId") Long userId) {
        List<Collection> collections = collectionRepository.findByUserId(userId);
        return new ResponseEntity<>(collections, HttpStatus.OK);
    }

    @ApiOperation(value = "获取一条收藏歌曲")

    //   获取一条收藏歌曲
    @GetMapping("/collection/{id}")
    public ResponseEntity getCollectionMusic(@PathVariable("id") Long id) {
        Collection collection = collectionRepository.findOne(id);

        //每次点击歌单里的歌曲，播放量+1
        collection.setPlayCount(collection.getPlayCount() + 1);
        return new ResponseEntity<>(collection, HttpStatus.OK);
    }

    //   添加一条收藏歌曲
    @PostMapping("/collection")
    public ResponseEntity addCollectionMusic(@RequestBody Collection collection) {
        collectionRepository.save(collection);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    //   删除一条收藏歌曲
    @DeleteMapping("/collection/{id}")
    public ResponseEntity deleteCollectionMusic(@PathVariable("id") Long id) {
        collectionRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //    评分 0-5
    @PutMapping("/collection")
    public ResponseEntity musicScore(@RequestBody Collection collection) {
        Collection collection1 = collectionRepository.findOne(collection.getId());
        collection1.setLove(collection.getLove());
        collectionRepository.save(collection1);

        MusicLeaderBoard musicLeaderBoard = musicLeaderRepository.findByMusicId(collection.getMusicId());
        musicLeaderBoard.setHot(musicLeaderBoard.getHot() + collection.getLove()); //根据musicId 设置排行榜

        return new ResponseEntity<>(HttpStatus.OK);
    }

    //    推荐歌曲
    @GetMapping("/recommend/{userId}")
    public ResponseEntity recommendMusic(@PathVariable("userId") Long userId) {
        Collection collections1 = (Collection) collectionRepository.findAll();
        if(collections1==null){
            
        }
        List<Collection> collections = collectionRepository.findByUserIdOrderByLove(userId);

        List<Long> firstMusics = new ArrayList<>();
        List<Long> secondMusics = new ArrayList<>();
        List<Long> thirdMusics = new ArrayList<>();
        List<Long> fourMusics = new ArrayList<>();
        List<Long> fiveMusics = new ArrayList<>();
        List<Long> sixMusics = new ArrayList<>();
        List<Long> sevenMusics = new ArrayList<>();


        // 1
        collections.forEach(collection -> {
            if (firstMusics.size() == 50) return;
            firstMusics.add(collection.getMusicId());  //找到自己评分高的50音乐
        });

        List<Map> signers = new ArrayList<>();
        firstMusics.forEach(id -> {

//            HashMap<Long, Integer> signerMap = new HashMap<Long, Integer>();
//            for (Map signerId : signers) {
//                if (signerMap.get(signerId) != null) {
//                    Integer value = signerMap.get(signerId);
//                    signerMap.put(signerId, value+1);
//                } else {
//                    signerMap.put(signerId, 1);
//                }
//            }

            Map singerMap = new HashMap();
            Music music1 = musicRepository.findOne(id);
            Long signerId = music1.getSingerId();
            signers.forEach(s -> {
                if (s.get("signerId") == signerId) {
                    Long count = (Long) s.get("count") + 1;
                    //todo 统计出现次数并排序
                } else {
                    singerMap.put(signerId,1);
                    signers.add(singerMap);                   //根据50首歌曲找到相应歌手Id，然后构造对象{歌手,出现次数}，得到喜欢的歌手排行榜
                }
            });
        });

        //找到最喜欢的歌手
        Long mostLovedSignerId = (Long) signers.get(0).get("signerId");   //第一位歌手
        Signer signer = signerRepository.findOne(mostLovedSignerId);
        String mostLovedSignerTag = signer.getTag();

        //2 歌手类别
        List<MusicLeaderBoard> signerLeaderBoard2 = musicLeaderRepository.findByTagOrderByHot(mostLovedSignerTag);   //从歌曲排行榜中找到该类别
        signerLeaderBoard2.forEach(s -> {
            if (secondMusics.size() == 50) return;
            secondMusics.add(s.getMusicId());  //根据喜欢的歌手类别找出评分高的50音乐
        });

        //todo 歌手排行

        Long signerId = signerLeaderBoard2.get(0).getSignerId();          //从歌手排行榜中找到该类别的第一位歌手

        // 3类似歌手的歌曲类别
        List<MusicLeaderBoard> signerLeaderBoard3 = musicLeaderRepository.findBySignerIdOrderByHot(signerId);            //找到该歌手的音乐
        signerLeaderBoard3.forEach(s -> {
            if (thirdMusics.size() == 50) return;
            thirdMusics.add(s.getMusicId());  //找到类似tag的第一位歌手的50首音乐
        });


        List<Collection> collections11 = collectionRepository.findByUserIdOrderByPlayCount(userId);
        Long mostLoveMusicId = collections11.get(0).getMusicId();  //播放量最高的音乐

        //基本音乐
        Music music = musicRepository.findOne(mostLoveMusicId);   //将播放量最高的音乐作为基本音乐

        // 4 基本音乐的歌手排行
        Long mostLoveMusicSignerId = music.getSingerId();
        List<MusicLeaderBoard> musicLeaderBoards4 = musicLeaderRepository.findBySignerIdOrderByHot(mostLoveMusicSignerId);//找到歌手的所有歌曲按热度排序
        musicLeaderBoards4.forEach(s -> {
            if (fourMusics.size() == 50) return;
            fourMusics.add(s.getMusicId());  //找到该歌手的50首音乐
        });


        //5 基本音乐的type排行
        String type = music.getType();
        List<MusicLeaderBoard> musicLeaderBoards5 = musicLeaderRepository.findByTypeOrderByHot(type);//找到该类型的所有歌曲按热度排序
        musicLeaderBoards5.forEach(s -> {
            if (fiveMusics.size() == 50) return;
            fiveMusics.add(s.getMusicId());  //找到该类型的50首音乐
        });

        //6 热评
        List<Comment> comments = commentRepository.findByMusicIdOrderByIsLike(music.getId());
        Long commentId = comments.get(0).getCommentedId();  //热评的人
        List<Collection> collections2 = collectionRepository.findByUserIdOrderByPlayCount(commentId); //根据播放量找到此人最喜欢的音乐
        collections.forEach(c -> {
            if (sixMusics.size() == 50) return;
            sixMusics.add(c.getMusicId());   //找到该人最喜欢的50首音乐
        });

        //7 选择10首基本音乐，选出同样喜欢这些音乐的人，找到他们喜欢的音乐
        List<Long> mostPlayMusicArray = new ArrayList<>();
        collections11.forEach(c -> {
            if (mostPlayMusicArray.size() == 10) return;
            mostPlayMusicArray.add(c.getMusicId());
        });

        //每首歌选择最喜欢的5个人，一共50个人,存在数组mostLikeMusicUserId中，找出这些人中出现次数最多的

        List<Long> mostLikeMusicUserId = new ArrayList<>();
        mostPlayMusicArray.forEach(musicId -> {
            List<Collection> collections3 = collectionRepository.findByMusicIdOrderByPlayCount(musicId);

            List<Long> temp = new ArrayList<>();
            collections3.forEach(c -> {
                if (temp.size() == 5) return;
                temp.add(c.getUserId());
            });
            mostLikeMusicUserId.addAll(temp);
        });

        //构造一个数组对象来存储用户出现次数
        List<Map> mostLikeMusicUserIdArray = new ArrayList<>();
        mostLikeMusicUserId.forEach(m -> {

            Map mostLikeUserMap = new HashMap();
            mostLikeMusicUserIdArray.forEach(s -> {
                //todo 判断数组 length == 0
                if (s.get("userId") == m) {
                    //todo 统计出现次数并排序
                } else {
                    mostLikeUserMap.put("userId", m);
                    mostLikeUserMap.put("count", 1);
                    mostLikeMusicUserIdArray.add(mostLikeUserMap);
                }
            });
        });

        //最相似用户
        Long mostLikeUserId = (Long) mostLikeMusicUserIdArray.get(0).get("userId");
        List<Collection> collections4 = collectionRepository.findByUserIdOrderByPlayCount(mostLikeUserId);
        collections4.forEach(c -> {
            if (sevenMusics.size() == 50) return;
            sevenMusics.add(c.getMusicId());   //找到该人最喜欢的50首音乐
        });


        List<Long> array = new ArrayList<>();
        array.addAll(firstMusics);
        array.addAll(secondMusics);
        array.addAll(thirdMusics);
        array.addAll(fourMusics);
        array.addAll(fiveMusics);
        array.addAll(sixMusics);
        array.addAll(sevenMusics);


        //todo 求数组的交集,构造出现次数最多的arrayMap,找到前50，
        List<Long> arrayMap = new ArrayList<>();
        List<Long> myCollectionMusicId = new ArrayList<>();

        List<Collection> myCollection = collectionRepository.findByUserId(userId);
        myCollection.forEach(m -> {
            myCollectionMusicId.add(m.getMusicId());
        });

        //再把已经是自己歌单里面的删除
        arrayMap.forEach(a -> {
//            if(myCollectionMusicId.contains(a.get("musicId")));
            arrayMap.remove(a);
        });


        //生成用户口味，遍历歌单myCollectionMusicId，从MusicStyle里面，找出这首歌出现最高的type，作为这首歌的type，给该用户的type+1

        List<Map> userStyleList = new ArrayList<>();

        Map userStyle = new HashMap();

        myCollectionMusicId.forEach(c -> {
            List<Integer> type1 = new ArrayList<>();

            MusicStyle music1 = musicStyleRepository.findOneByMusicId(c);
            Integer rap = music1.getRap();
            Integer blues = music1.getBlues();
            Integer classical = music1.getClassical();
            Integer folk = music1.getFolk();
            Integer heavyMetal = music1.getHeavyMetal();
            Integer hiphop = music1.getHiphop();
            Integer jazz = music1.getJazz();
            Integer light = music1.getLight();
            Integer pop = music1.getPop();
            Integer rock = music1.getRock();

            //todo 排序 找到最大的存为[{key：value}]

            String top = "1";
            switch (top) {
                case "rap":
                    userStyleList.forEach(user -> {
                        if (Objects.isNull(user.get("rap"))) {
                            userStyle.put("rap", 1);
                        } else {
                            //找到该属性，给值+1
                        }
                    });
                    break;
                //todo

            }
            userStyleList.add(userStyle);
        });


        //潜在因子,根据用户的口味，以及歌曲的风格

        //张三对arrayMap里面的音乐的喜欢程度，存放在一个数组对象里面
        List<Map> myLikeDegreeList = new ArrayList<>();
        Map myLikeDegreeMap = new HashMap();

        arrayMap.forEach(musicId -> {
            MusicStyle music1 = musicStyleRepository.findOneByMusicId(musicId);

            Integer rap = music1.getRap();
            Integer blues = music1.getBlues();
            Integer classical = music1.getClassical();
            Integer folk = music1.getFolk();
            Integer heavyMetal = music1.getHeavyMetal();
            Integer hiphop = music1.getHiphop();
            Integer jazz = music1.getJazz();
            Integer light = music1.getLight();
            Integer pop = music1.getPop();
            Integer rock = music1.getRock();

            userStyleList.forEach(user -> {
                Integer myRap = (Integer) user.get("rap");
                Integer myBlues = (Integer) user.get("blues");
                Integer myClassical = (Integer) user.get("classical");
                Integer myFolk = (Integer) user.get("folk");
                Integer myHeavyMetal = (Integer) user.get("heavyMetal");
                Integer myHiphop = (Integer) user.get("hiphop");
                Integer myJazz = (Integer) user.get("jazz");
                Integer myLight = (Integer) user.get("light");
                Integer myPop = (Integer) user.get("pop");
                Integer myRock = (Integer) user.get("rock");

                Integer myLikeMusic = myRap * rap + myBlues * blues + myClassical * classical +
                        myFolk * folk + myHeavyMetal * heavyMetal + myHiphop * hiphop +
                        myJazz * jazz + myLight * light + myPop * pop + myRock * rock;

                myLikeDegreeMap.put("musicId",musicId);
                myLikeDegreeMap.put("degree", myLikeMusic);
            });

            myLikeDegreeList.add(myLikeDegreeMap);
        });

        // todo 从myLikeDegreeList里面找到degree最高的前五个推荐给该用户

        return new ResponseEntity<>(HttpStatus.OK);
    }
}


//myCollection：我的歌单列表
//myCollectionMusicId：我的歌单musicId [id,id,,,]
//collections：我的歌单评分列表 array
//collections11：我的歌单播放量列表
//collections2：热评得到的类似的人的歌单播放量列表
//collections3：这10首音乐的播放量列表
//mostPlayMusicArray：我的歌单播放量最高的10首音乐
//mostLikeMusicUserId：喜欢播放这10首音乐的前50个人
//mostLikeMusicUserIdArray：统计出现次数最多的人[mostLikeUserMap:{userId,count},{},,]
//collections4：播放10首歌得到类似的人的歌单播放量列表
//signers：我喜欢的歌手列表[singerMap:{signerId,count},{}]
//music：基本音乐
//comments：基本音乐的热评
//firstMusics：自己评分高的50音乐 array[id,id,,,]
//secondMusics:最喜欢的歌手的类别评分高的50音乐
//thirdMusics：类似歌手该类别评分高的50音乐
//fourMusics：基本音乐的歌手评分高的50音乐
//fiveMusics：基本音乐类别评分高的50音乐
//sixMusics：热评类似的人播放量高的50音乐
//sevenMusics：10首歌类似的人播放量高的50音乐
//array：所有可能的350首音乐
//arrayMap:要推荐给用户的若干歌曲，除去自己列表中有的,里面存的是[{musicId,count}] ?
//userStyleList :用户口味[userStyle{"rap",100},{"pop":10}]
//myLikeDegreeList:我对该音乐的喜欢程度[myLikeDegreeMap{musicId,degree}]
